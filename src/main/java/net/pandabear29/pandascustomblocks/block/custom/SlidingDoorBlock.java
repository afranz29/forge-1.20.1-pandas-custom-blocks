package net.pandabear29.pandascustomblocks.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;

public class SlidingDoorBlock extends HorizontalDirectionalBlock {

    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    public static final BooleanProperty OPEN = BlockStateProperties.OPEN;
    public static final EnumProperty<DoorHingeSide> HINGE = BlockStateProperties.DOOR_HINGE;
    public static final EnumProperty<DoorPart> PART = EnumProperty.create("part", DoorPart.class);

    // Voxel Shapes for a thin door (3 pixels thick)
    protected static final VoxelShape NORTH_SHAPE = Block.box(0, 0, 0, 16, 16, 3);
    protected static final VoxelShape SOUTH_SHAPE = Block.box(0, 0, 13, 16, 16, 16);
    protected static final VoxelShape WEST_SHAPE = Block.box(0, 0, 0, 3, 16, 16);
    protected static final VoxelShape EAST_SHAPE = Block.box(13, 0, 0, 16, 16, 16);


    public SlidingDoorBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(OPEN, false).setValue(HINGE, DoorHingeSide.LEFT).setValue(PART, DoorPart.BOTTOM_LEFT));
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        if (!pState.getValue(OPEN)) {
                return switch (pState.getValue(FACING)) {
                    case NORTH -> NORTH_SHAPE;
                    case SOUTH -> SOUTH_SHAPE;
                    case WEST -> WEST_SHAPE;
                    case EAST -> EAST_SHAPE;
                    default -> Shapes.block();
                };
        } else {
            // When open, the door is passable. The visual model will handle the look.
            return Shapes.empty();
        }
    }


    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(FACING, OPEN, HINGE, PART);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        Level level = pContext.getLevel();
        BlockPos clickedPos = pContext.getClickedPos();
        Direction facing = pContext.getHorizontalDirection().getOpposite();

        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 2; x++) {
                BlockPos pos = clickedPos.relative(facing.getClockWise(), x).above(y);
                if (!level.getBlockState(pos).canBeReplaced(pContext)) {
                    return null;
                }
            }
        }
        return this.defaultBlockState().setValue(FACING, facing).setValue(HINGE, this.getHinge(pContext));
    }

    @Override
    public void onPlace(BlockState pState, Level pLevel, BlockPos pPos, BlockState pOldState, boolean pIsMoving) {
        if (pOldState.is(pState.getBlock()) || pIsMoving) {
            return;
        }

        if (pState.getValue(PART) == DoorPart.BOTTOM_LEFT) {
            Direction facing = pState.getValue(FACING);
            for (int y = 0; y < 4; y++) {
                for (int x = 0; x < 2; x++) {
                    if (x == 0 && y == 0) continue;

                    BlockPos currentPos = pPos.relative(facing.getClockWise(), x).above(y);
                    pLevel.setBlock(currentPos, pState.setValue(PART, DoorPart.from(x, y)), 3);
                }
            }
        }
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        BlockPos basePos = getBasePos(pState, pPos);
        boolean isOpen = !pState.getValue(OPEN);
        Direction facing = pLevel.getBlockState(basePos).getValue(FACING); // Get facing from the base block

        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 2; x++) {
                BlockPos currentPos = basePos.relative(facing.getClockWise(), x).above(y);
                BlockState currentState = pLevel.getBlockState(currentPos);
                if (currentState.is(this)) {
                    pLevel.setBlock(currentPos, currentState.setValue(OPEN, isOpen), 10);
                }
            }
        }
        pLevel.levelEvent(pPlayer, isOpen ? 1012 : 1006, pPos, 0);
        return InteractionResult.sidedSuccess(pLevel.isClientSide);
    }

    @Override
    public void playerWillDestroy(Level pLevel, BlockPos pPos, BlockState pState, Player pPlayer) {
        if (!pLevel.isClientSide) {
            BlockPos basePos = getBasePos(pState, pPos);
            Direction facing = pLevel.getBlockState(basePos).getValue(FACING); // Get facing from the base block

            for (int y = 0; y < 4; y++) {
                for (int x = 0; x < 2; x++) {
                    BlockPos partPos = basePos.relative(facing.getClockWise(), x).above(y);
                    if (!partPos.equals(pPos) && pLevel.getBlockState(partPos).is(this)) {
                        pLevel.destroyBlock(partPos, !pPlayer.isCreative(), pPlayer);
                    }
                }
            }
        }
        super.playerWillDestroy(pLevel, pPos, pState, pPlayer);
    }

    private BlockPos getBasePos(BlockState pState, BlockPos pPos) {
        DoorPart part = pState.getValue(PART);
        Direction facing = pState.getValue(FACING);
        // This logic was slightly flawed. Let's correct it to be more robust.
        int xOffset = part.getX();
        int yOffset = part.getY();

        return pPos.relative(facing.getCounterClockWise(), xOffset).below(yOffset);
    }

    private DoorHingeSide getHinge(BlockPlaceContext pContext) {
        Direction facing = pContext.getHorizontalDirection().getOpposite();
        BlockPos clickedPos = pContext.getClickedPos();

        if (facing.getAxis() == Direction.Axis.Z) {
            double clickX = pContext.getClickLocation().x - clickedPos.getX();
            return (facing == Direction.NORTH && clickX < 0.5D) || (facing == Direction.SOUTH && clickX > 0.5D) ? DoorHingeSide.LEFT : DoorHingeSide.RIGHT;
        } else {
            double clickZ = pContext.getClickLocation().z - clickedPos.getZ();
            return (facing == Direction.WEST && clickZ > 0.5D) || (facing == Direction.EAST && clickZ < 0.5D) ? DoorHingeSide.LEFT : DoorHingeSide.RIGHT;
        }
    }
}