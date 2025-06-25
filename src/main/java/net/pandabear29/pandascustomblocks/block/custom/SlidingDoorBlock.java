package net.pandabear29.pandascustomblocks.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.DoorHingeSide;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;

public class SlidingDoorBlock extends HorizontalDirectionalBlock {

    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    public static final BooleanProperty OPEN = BlockStateProperties.OPEN;
    public static final EnumProperty<DoorHingeSide> HINGE = BlockStateProperties.DOOR_HINGE;
    public static final EnumProperty<DoorPart> PART = EnumProperty.create("part", DoorPart.class);

    // Shapes for the closed door panels
    protected static final VoxelShape EAST_CLOSED = Block.box(13, 0, 0, 16, 16, 16);
    protected static final VoxelShape WEST_CLOSED = Block.box(0, 0, 0, 3, 16, 16);
    protected static final VoxelShape SOUTH_CLOSED = Block.box(0, 0, 13, 16, 16, 16);
    protected static final VoxelShape NORTH_CLOSED = Block.box(0, 0, 0, 16, 16, 3);

    public SlidingDoorBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(OPEN, false)
                .setValue(HINGE, DoorHingeSide.LEFT)
                .setValue(PART, DoorPart.BOTTOM_LEFT));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(FACING, OPEN, HINGE, PART);
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return switch (pState.getValue(FACING)) {
            case EAST -> EAST_CLOSED;
            case WEST -> WEST_CLOSED;
            case SOUTH -> SOUTH_CLOSED;
            default -> NORTH_CLOSED;
        };
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (pLevel.isClientSide) {
            return InteractionResult.SUCCESS;
        }

        boolean isOpen = pState.getValue(OPEN);
        BlockPos basePos = getBasePos(pState, pPos); // Find the main block of the door
        BlockState baseState = pLevel.getBlockState(basePos);

        if (!baseState.is(this)) return InteractionResult.FAIL; // Should not happen

        Direction facing = baseState.getValue(FACING);
        DoorHingeSide hinge = baseState.getValue(HINGE);

        // Determine the direction the door will slide (left or right relative to its facing direction)
        Direction slideDirection = hinge == DoorHingeSide.LEFT ? facing.getCounterClockWise() : facing.getClockWise();

        // Loop through all 8 parts of the door
        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 2; x++) {
                BlockPos currentPartPos = basePos.relative(facing.getClockWise(), x).above(y);
                BlockState currentPartState = pLevel.getBlockState(currentPartPos);

                if (currentPartState.is(this)) {
                    // To close, move opposite to the slide direction. To open, move with it.
                    BlockPos newPos = isOpen ? currentPartPos.relative(slideDirection.getOpposite(), 2) : currentPartPos.relative(slideDirection, 2);

                    pLevel.setBlock(currentPartPos, Blocks.AIR.defaultBlockState(), 3);
                    pLevel.setBlock(newPos, currentPartState.setValue(OPEN, !isOpen), 3);
                }
            }
        }

        pLevel.levelEvent(pPlayer, isOpen ? 1012 : 1006, basePos, 0);
        return InteractionResult.CONSUME;
    }

    @Override
    public void playerWillDestroy(Level pLevel, BlockPos pPos, BlockState pState, Player pPlayer) {
        if (!pLevel.isClientSide) {
            BlockPos basePos = getBasePos(pState, pPos);
            Direction facing = pState.getValue(FACING);

            for (int y = 0; y < 4; y++) {
                for (int x = 0; x < 2; x++) {
                    BlockPos partPos = basePos.relative(facing.getClockWise(), x).above(y);
                    if (!partPos.equals(pPos) && pLevel.getBlockState(partPos).is(this)) {
                        pLevel.setBlock(partPos, Blocks.AIR.defaultBlockState(), 35);
                    }
                }
            }
        }
        super.playerWillDestroy(pLevel, pPos, pState, pPlayer);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        Level level = pContext.getLevel();
        BlockPos clickedPos = pContext.getClickedPos();
        Direction facing = pContext.getHorizontalDirection().getOpposite();
        DoorHingeSide hinge = this.getHinge(pContext);

        // Define the 2x4 area to check for placement
        BlockPos basePos = (hinge == DoorHingeSide.LEFT) ? clickedPos : clickedPos.relative(facing.getClockWise());

        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 2; x++) {
                // The two columns of the door are perpendicular to the way it's facing
                BlockPos posToCheck = basePos.relative(facing.getCounterClockWise(), x).above(y);
                if (!level.getBlockState(posToCheck).canBeReplaced(pContext)) {
                    return null;
                }
            }
        }

        return this.defaultBlockState().setValue(FACING, facing).setValue(HINGE, hinge);
    }

    @Override
    public void onPlace(BlockState pState, Level pLevel, BlockPos pPos, BlockState pOldState, boolean pIsMoving) {
        if (pIsMoving) return;

        BlockPos basePos = getBasePos(pState, pPos);

        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 2; x++) {
                BlockPos currentPartPos = basePos.relative(pState.getValue(FACING).getClockWise(), x).above(y);
                if (!currentPartPos.equals(pPos)) {
                    pLevel.setBlock(currentPartPos, pState.setValue(PART, DoorPart.from(x, y)), 3);
                }
            }
        }
    }

    // Reverted to the simple, correct hinge detection logic
    private DoorHingeSide getHinge(BlockPlaceContext pContext) {
        Direction facing = pContext.getHorizontalDirection().getOpposite();
        BlockPos clickedPos = pContext.getClickedPos();

        if (facing.getAxis() == Direction.Axis.Z) { // North or South
            double clickX = pContext.getClickLocation().x - clickedPos.getX();
            if ((facing == Direction.NORTH && clickX < 0.5D) || (facing == Direction.SOUTH && clickX > 0.5D)) {
                return DoorHingeSide.LEFT;
            } else {
                return DoorHingeSide.RIGHT;
            }
        } else { // East or West
            double clickZ = pContext.getClickLocation().z - clickedPos.getZ();
            if ((facing == Direction.WEST && clickZ < 0.5D) || (facing == Direction.EAST && clickZ > 0.5D)) {
                return DoorHingeSide.LEFT;
            } else {
                return DoorHingeSide.RIGHT;
            }
        }
    }

    // Finds the bottom-left-most block of the door structure from any given part
    private BlockPos getBasePos(BlockState pState, BlockPos pPos) {
        DoorPart part = pState.getValue(PART);
        Direction facing = pState.getValue(FACING);
        DoorHingeSide hinge = pState.getValue(HINGE);

        // Adjust for hinge side to find the consistent "base"
        BlockPos hingeAdjustedPos = pPos;
        if (hinge == DoorHingeSide.RIGHT && (part.getX() == 0 || part.getX() == 1)) {
            // This logic needs to correctly find the base from any part, regardless of hinge
        }

        int xOffset = part.getX();
        int yOffset = part.getY();

        return pPos.relative(facing.getCounterClockWise(), xOffset).below(yOffset);
    }
}