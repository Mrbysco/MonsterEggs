package com.mrbysco.monstereggs.block;

import com.mrbysco.monstereggs.config.EggConfig;
import com.mrbysco.monstereggs.registry.EggRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.InsideBlockEffectApplier;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NonNull;

import java.util.function.Supplier;

public class MonsterEggBlock extends Block implements SimpleWaterloggedBlock {
	public static final BooleanProperty HANGING = BlockStateProperties.HANGING;
	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

	protected static final VoxelShape SHAPE = Block.box(3, 0, 3, 13, 12, 13);
	protected static final VoxelShape HANGING_SHAPE = Block.box(3, 4, 3, 13, 16, 13);

	private final Supplier<? extends EntityType<? extends Mob>> typeSupplier;

	public MonsterEggBlock(Supplier<? extends EntityType<? extends Mob>> type, Properties properties) {
		super(properties);
		this.typeSupplier = type;
		this.registerDefaultState(this.stateDefinition.any().setValue(HANGING, Boolean.FALSE).setValue(WATERLOGGED, Boolean.FALSE));
	}

	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext placeContext) {
		Level level = placeContext.getLevel();
		BlockPos clickedPos = placeContext.getClickedPos();
		FluidState fluidstate = level.getFluidState(clickedPos);

		BlockState blockstate = this.defaultBlockState().setValue(HANGING, false)
				.setValue(WATERLOGGED, fluidstate.getType() == Fluids.WATER);
		if (!level.getBlockState(clickedPos.below()).isAir()) {
			return blockstate;
		} else {
			if (!level.getBlockState(clickedPos.above()).isAir()) {
				blockstate = blockstate.setValue(HANGING, true);
				return blockstate;
			}
		}

		return null;
	}

	@Override
	public boolean canSurvive(@NonNull BlockState state, LevelReader reader, BlockPos pos) {
		BlockPos belowPos = pos.below();
		BlockState belowState = reader.getBlockState(belowPos);
		BlockPos abovePos = pos.above();
		BlockState aboveState = reader.getBlockState(abovePos);
		return belowState.isFaceSturdy(reader, belowPos, Direction.UP) || aboveState.isFaceSturdy(reader, abovePos, Direction.DOWN);
	}

	@Override
	protected @NonNull BlockState updateShape(@NonNull BlockState state, LevelReader level, @NonNull ScheduledTickAccess scheduledTickAccess,
	                                          BlockPos pos, @NonNull Direction direction, @NonNull BlockPos neighborPos, @NonNull BlockState neighborState,
	                                          @NonNull RandomSource random) {
		if (!level.getBlockState(pos.below()).isAir()) {
			state = state.setValue(HANGING, false);
		} else if (!level.getBlockState(pos.above()).isAir()) {
			state = state.setValue(HANGING, true);
		}
		return !state.canSurvive(level, pos) ? Blocks.AIR.defaultBlockState() :
				super.updateShape(state, level, scheduledTickAccess, pos, direction, neighborPos, neighborState, random);
	}

	@Override
	protected void createBlockStateDefinition(Builder<Block, BlockState> blockStateBuilder) {
		blockStateBuilder.add(HANGING, WATERLOGGED);
	}

	public @NonNull VoxelShape getShape(BlockState state, @NonNull BlockGetter getter, @NonNull BlockPos pos, @NonNull CollisionContext collisionContext) {
		if (state.getValue(HANGING)) {
			return HANGING_SHAPE;
		} else {
			return SHAPE;
		}
	}

	public EntityType<?> getType() {
		return typeSupplier.get();
	}

	@Override
	public void fallOn(@NonNull Level level, @NonNull BlockState state, @NonNull BlockPos pos, @NonNull Entity entity, double fallDistance) {
		super.fallOn(level, state, pos, entity, fallDistance);
		if (fallDistance > 1 && level.getRandom().nextBoolean()) {
			destroyEgg(level, state, pos, entity);
		}
	}

	@Override
	public void stepOn(@NonNull Level level, @NonNull BlockPos pos, @NonNull BlockState state, @NonNull Entity entity) {
		if (!(entity instanceof Player) && level.getRandom().nextBoolean()) {
			destroyEgg(level, state, pos, entity);
		}
	}

	@Override
	protected void entityInside(@NonNull BlockState state, @NonNull Level level, @NonNull BlockPos pos, @NonNull Entity entity, @NonNull InsideBlockEffectApplier applier, boolean intersects) {
		if (!(entity instanceof Player) && level.getRandom().nextBoolean()) {
			destroyEgg(level, state, pos, entity);
		}
	}

	@Override
	public void playerDestroy(@NonNull ServerLevel level, @NonNull ServerPlayer player, @NonNull BlockPos pos, @NonNull BlockState state, @Nullable BlockEntity blockEntity, @NonNull ItemStack stack) {
		super.playerDestroy(level, player, pos, state, blockEntity, stack);
		destroyEgg(level, state, pos, player);
	}

	private void destroyEgg(Level level, BlockState state, BlockPos pos, Entity entity) {
		if (level instanceof ServerLevel serverLevel) {
			level.playSound(null, pos, EggRegistry.MONSTER_EGG_BROKEN.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
			level.destroyBlock(pos, false, entity);

			EntityType<?> eggType = this.getType();
			Entity eggEntity = eggType.create(serverLevel, EntitySpawnReason.DISPENSER);
			if (eggEntity != null) {
				eggEntity.setPosRaw(pos.getX() + 0.5D, pos.getY() + EggConfig.COMMON.spawnOffset.get(), pos.getZ() + 0.5D);
				level.addFreshEntity(eggEntity);
			}
		}
	}

	/**
	 * Insert waterlogged bits
	 */
	@Override
	public @NonNull FluidState getFluidState(BlockState state) {
		return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(true) : super.getFluidState(state);
	}
}
