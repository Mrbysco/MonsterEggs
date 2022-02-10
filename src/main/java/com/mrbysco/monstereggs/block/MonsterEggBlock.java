package com.mrbysco.monstereggs.block;

import com.mrbysco.monstereggs.registry.EggRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class MonsterEggBlock extends Block {
	protected static final VoxelShape SHAPE = Block.box(3, 0, 3, 13, 12, 13);
	private final Supplier<? extends EntityType<? extends Mob>> typeSupplier;

	public MonsterEggBlock(Supplier<? extends EntityType<? extends Mob>> type, Properties properties) {
		super(properties);
		this.typeSupplier = type;
	}

	public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext collisionContext) {
		return SHAPE;
	}

	public EntityType<?> getType() {
		return typeSupplier.get();
	}

	@Override
	public void fallOn(Level level, BlockState state, BlockPos pos, Entity entity, float fallDistance) {
		super.fallOn(level, state, pos, entity, fallDistance);
		if(fallDistance > 1 && level.random.nextBoolean()) {
			destroyEgg(level, state, pos, entity);
		}
	}

	@Override
	public void playerDestroy(Level level, Player player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, ItemStack stack) {
		super.playerDestroy(level, player, pos, state, blockEntity, stack);
		destroyEgg(level, state, pos, player);
	}

	private void destroyEgg(Level level, BlockState state, BlockPos pos, Entity entity) {
		if(!level.isClientSide) {
			level.playSound(null, pos, EggRegistry.MONSTER_EGG_BROKEN.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
			level.destroyBlock(pos,false, entity);

			EntityType<?> eggType = this.getType();
			Entity eggEntity = eggType.create(level);
			if(eggEntity != null) {
				eggEntity.setPosRaw(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D);
				level.addFreshEntity(eggEntity);
			}
		}
	}

	@Override
	public boolean canSurvive(BlockState state, LevelReader reader, BlockPos pos) {
		BlockPos blockpos = pos.below();
		return canSupportRigidBlock(reader, blockpos);
	}
}
