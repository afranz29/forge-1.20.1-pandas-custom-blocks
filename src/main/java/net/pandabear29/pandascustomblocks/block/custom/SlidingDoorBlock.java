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

    // --- Voxel Shape Definitions ---
    // These shapes match the visual models.

    protected static final VoxelShape NORTH_CLOSED = Block.box(0, 0, 0, 16, 16, 3);
    protected static final VoxelShape SOUTH_CLOSED = Block.box(0, 0, 13, 16, 16, 16);
    protected static final VoxelShape WEST_CLOSED  = Block.box(0, 0, 0, 3, 16, 16);
    protected static final VoxelShape EAST_CLOSED  = Block.box(13, 0, 0, 16, 16, 16);

    protected static final VoxelShape NORTH_OPEN_LEFT    = Block.box(-14, 0, 0, 2, 16, 3);
    protected static final VoxelShape NORTH_OPEN_RIGHT   = Block.box(14, 0, 0, 30, 16, 3);
    protected static final VoxelShape SOUTH_OPEN_LEFT    = Block.box(-14, 0, 13, 2, 16, 16);
    protected static final VoxelShape SOUTH_OPEN_RIGHT   = Block.box(14, 0, 13, 30, 16, 16);
    protected static final VoxelShape WEST_OPEN_LEFT     = Block.box(0, 0, 14, 3, 16, 30);
    protected static final VoxelShape WEST_OPEN_RIGHT    = Block.box(0, 0, -14, 3, 16, 2);
    protected static final VoxelShape EAST_OPEN_LEFT     = Block.box(13, 0, -14, 16, 16, 2);
    protected static final VoxelShape EAST_OPEN_RIGHT    = Block.box(13, 0, 14, 16, 16, 30);


    public SlidingDoorBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(OPEN, false).setValue(HINGE, DoorHingeSide.LEFT).setValue(PART, DoorPart.BOTTOM_LEFT));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(FACING, OPEN, HINGE, PART);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        // The collision shape is what you bump into. When the door is open, this should be empty.
        if (pState.getValue(OPEN)) {
            return Shapes.empty();
        }

        return switch (pState.getValue(FACING)) {
            case NORTH -> NORTH_CLOSED;
            case SOUTH -> SOUTH_CLOSED;
            case WEST -> WEST_CLOSED;
            case EAST -> EAST_CLOSED;
            default -> Shapes.block();
        };
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        // The selection shape (what you can see with the F3 debug crosshair).
        // This needs to match where the door *is*, even when open.
        if (pState.getValue(OPEN)) {
            Direction facing = pState.getValue(FACING);
            DoorHingeSide hinge = pState.getValue(HINGE);

            // This logic now correctly mirrors the final `variants` blockstate file.
            switch(facing) {
                case NORTH: return hinge == DoorHingeSide.LEFT ? NORTH_OPEN_LEFT : NORTH_OPEN_RIGHT;
                case SOUTH: return hinge == DoorHingeSide.LEFT ? SOUTH_OPEN_RIGHT : SOUTH_OPEN_LEFT;
                case WEST:  return hinge == DoorHingeSide.LEFT ? WEST_OPEN_RIGHT : WEST_OPEN_LEFT;
                case EAST:  return hinge == DoorHingeSide.LEFT ? EAST_OPEN_LEFT : EAST_OPEN_RIGHT;
            }
        }
        return getCollisionShape(pState, pLevel, pPos, pContext);
    }

    // The rest of the file remains the same...

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        Level level = pContext.getLevel();
        BlockPos clickedPos = pContext.getClickedPos();
        Direction facing = pContext.getHorizontalDirection().getOpposite();
        DoorHingeSide hinge = this.getHinge(pContext);

        BlockPos basePos = (hinge == DoorHingeSide.LEFT) ? clickedPos : clickedPos.relative(facing.getCounterClockWise());

        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 2; x++) {
                BlockPos posToCheck = basePos.relative(facing.getClockWise(), x).above(y);
                if (!level.getBlockState(posToCheck).canBeReplaced(pContext)) {
                    return null;
                }
            }
        }

        DoorPart partToPlace = (hinge == DoorHingeSide.LEFT) ? DoorPart.BOTTOM_LEFT : DoorPart.BOTTOM_RIGHT;
        return this.defaultBlockState().setValue(FACING, facing).setValue(HINGE, hinge).setValue(PART, partToPlace);
    }

    @Override
    public void onPlace(BlockState pState, Level pLevel, BlockPos pPos, BlockState pOldState, boolean pIsMoving) {
        if (pOldState.is(pState.getBlock()) || pIsMoving) {
            return;
        }

        Direction facing = pState.getValue(FACING);
        BlockPos basePos = getBasePos(pState, pPos);

        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 2; x++) {
                BlockPos currentPartPos = basePos.relative(facing.getClockWise(), x).above(y);
                if (!currentPartPos.equals(pPos)) {
                    BlockState partState = pState.setValue(PART, DoorPart.from(x, y));
                    pLevel.setBlock(currentPartPos, partState, 3);
                }
            }
        }
    }

    private DoorHingeSide getHinge(BlockPlaceContext pContext) {
        Direction facing = pContext.getHorizontalDirection().getOpposite();
        BlockPos clickedPos = pContext.getClickedPos();

        if (facing.getAxis() == Direction.Axis.Z) {
            double clickX = pContext.getClickLocation().x - clickedPos.getX();
            if ((facing == Direction.NORTH && clickX < 0.5D) || (facing == Direction.SOUTH && clickX > 0.5D)) {
                return DoorHingeSide.RIGHT;
            } else {
                return DoorHingeSide.LEFT;
            }
        } else {
            double clickZ = pContext.getClickLocation().z - clickedPos.getZ();
            if ((facing == Direction.WEST && clickZ > 0.5D) || (facing == Direction.EAST && clickZ < 0.5D)) {
                return DoorHingeSide.RIGHT;
            } else {
                return DoorHingeSide.LEFT;
            }
        }
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        BlockPos basePos = getBasePos(pState, pPos);
        boolean isOpen = !pState.getValue(OPEN);
        BlockState baseState = pLevel.getBlockState(basePos);
        if (!baseState.is(this)) return InteractionResult.FAIL;

        Direction facing = baseState.getValue(FACING);

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
    public BlockState updateShape(BlockState pState, Direction pFacing, BlockState pFacingState, LevelAccessor pLevel, BlockPos pCurrentPos, BlockPos pFacingPos) {
        BlockPos basePos = getBasePos(pState, pCurrentPos);
        BlockPos partRelative = pCurrentPos.subtract(basePos);

        // If the block this part is attached to is destroyed, destroy the whole door.
        // This is a simplified check. A more robust check would see if all 8 blocks are still present.
        if (partRelative.getX() != 0 || partRelative.getY() != 0) { // If not the base block
            if (!pLevel.getBlockState(basePos).is(this)) {
                return Blocks.AIR.defaultBlockState();
            }
        }

        return super.updateShape(pState, pFacing, pFacingState, pLevel, pCurrentPos, pFacingPos);
    }

    @Override
    public void playerWillDestroy(Level pLevel, BlockPos pPos, BlockState pState, Player pPlayer) {
        if (!pLevel.isClientSide) {
            // Destroying any part of the door should destroy all other parts.
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

    private BlockPos getBasePos(BlockState pState, BlockPos pPos) {
        DoorPart part = pState.getValue(PART);
        Direction facing = pState.getValue(FACING);
        int xOffset = part.getX();
        int yOffset = part.getY();

        return pPos.relative(facing.getCounterClockWise(), xOffset).below(yOffset);
    }
}