package me.alpha432.oyvey.util;

import com.google.common.util.concurrent.AtomicDouble;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import me.alpha432.oyvey.OyVey;
import me.alpha432.oyvey.features.command.Command;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockDeadBush;
import net.minecraft.block.BlockFire;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockSnow;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.network.play.client.CPacketEntityAction.Action;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;

public class BlockUtill implements Util {

    public static final List blackList = Arrays.asList(new Block[] { Blocks.ENDER_CHEST, Blocks.CHEST, Blocks.TRAPPED_CHEST, Blocks.CRAFTING_TABLE, Blocks.ANVIL, Blocks.BREWING_STAND, Blocks.HOPPER, Blocks.DROPPER, Blocks.DISPENSER, Blocks.TRAPDOOR, Blocks.ENCHANTING_TABLE});
    public static final List shulkerList = Arrays.asList(new Block[] { Blocks.WHITE_SHULKER_BOX, Blocks.ORANGE_SHULKER_BOX, Blocks.MAGENTA_SHULKER_BOX, Blocks.LIGHT_BLUE_SHULKER_BOX, Blocks.YELLOW_SHULKER_BOX, Blocks.LIME_SHULKER_BOX, Blocks.PINK_SHULKER_BOX, Blocks.GRAY_SHULKER_BOX, Blocks.SILVER_SHULKER_BOX, Blocks.CYAN_SHULKER_BOX, Blocks.PURPLE_SHULKER_BOX, Blocks.BLUE_SHULKER_BOX, Blocks.BROWN_SHULKER_BOX, Blocks.GREEN_SHULKER_BOX, Blocks.RED_SHULKER_BOX, Blocks.BLACK_SHULKER_BOX});
    public static final List unSafeBlocks = Arrays.asList(new Block[] { Blocks.OBSIDIAN, Blocks.BEDROCK, Blocks.ENDER_CHEST, Blocks.ANVIL});
    public static List unSolidBlocks = Arrays.asList(new Block[] { Blocks.FLOWING_LAVA, Blocks.FLOWER_POT, Blocks.SNOW, Blocks.CARPET, Blocks.END_ROD, Blocks.SKULL, Blocks.FLOWER_POT, Blocks.TRIPWIRE, Blocks.TRIPWIRE_HOOK, Blocks.WOODEN_BUTTON, Blocks.LEVER, Blocks.STONE_BUTTON, Blocks.LADDER, Blocks.UNPOWERED_COMPARATOR, Blocks.POWERED_COMPARATOR, Blocks.UNPOWERED_REPEATER, Blocks.POWERED_REPEATER, Blocks.UNLIT_REDSTONE_TORCH, Blocks.REDSTONE_TORCH, Blocks.REDSTONE_WIRE, Blocks.AIR, Blocks.PORTAL, Blocks.END_PORTAL, Blocks.WATER, Blocks.FLOWING_WATER, Blocks.LAVA, Blocks.FLOWING_LAVA, Blocks.SAPLING, Blocks.RED_FLOWER, Blocks.YELLOW_FLOWER, Blocks.BROWN_MUSHROOM, Blocks.RED_MUSHROOM, Blocks.WHEAT, Blocks.CARROTS, Blocks.POTATOES, Blocks.BEETROOTS, Blocks.REEDS, Blocks.PUMPKIN_STEM, Blocks.MELON_STEM, Blocks.WATERLILY, Blocks.NETHER_WART, Blocks.COCOA, Blocks.CHORUS_FLOWER, Blocks.CHORUS_PLANT, Blocks.TALLGRASS, Blocks.DEADBUSH, Blocks.VINE, Blocks.FIRE, Blocks.RAIL, Blocks.ACTIVATOR_RAIL, Blocks.DETECTOR_RAIL, Blocks.GOLDEN_RAIL, Blocks.TORCH});

    public static List getBlockSphere(float breakRange, Class clazz) {
        NonNullList positions = NonNullList.create();

        positions.addAll((Collection) getSphere(EntityUtil.getPlayerPos(BlockUtill.mc.player), breakRange, (int) breakRange, false, true, 0).stream().filter((pos) -> {
            return clazz.isInstance(BlockUtill.mc.world.getBlockState(pos).getBlock());
        }).collect(Collectors.toList()));
        return positions;
    }

    public static void placeBlockScaffold(BlockPos pos) {
        Vec3d eyesPos = new Vec3d(BlockUtill.mc.player.posX, BlockUtill.mc.player.posY + (double) BlockUtill.mc.player.getEyeHeight(), BlockUtill.mc.player.posZ);
        EnumFacing[] aenumfacing = EnumFacing.values();
        int i = aenumfacing.length;

        for (int j = 0; j < i; ++j) {
            EnumFacing side = aenumfacing[j];
            BlockPos neighbor = pos.offset(side);
            EnumFacing side2 = side.getOpposite();
            Vec3d hitVec;

            if (canBeClicked(neighbor) && eyesPos.squareDistanceTo(hitVec = (new Vec3d(neighbor)).add(0.5D, 0.5D, 0.5D).add((new Vec3d(side2.getDirectionVec())).scale(0.5D))) <= 18.0625D) {
                BlockUtil3.faceVectorPacketInstant(hitVec);
                ProcessRightClickBlock(neighbor, side2, hitVec);
                BlockUtill.mc.player.swingArm(EnumHand.MAIN_HAND);
                BlockUtill.mc.rightClickDelayTimer = 4;
                return;
            }
        }

    }

    private static void ProcessRightClickBlock(BlockPos neighbor, EnumFacing side2, Vec3d hitVec) {}

    public static List getPossibleSides(BlockPos pos) {
        ArrayList facings = new ArrayList();
        EnumFacing[] aenumfacing = EnumFacing.values();
        int i = aenumfacing.length;

        for (int j = 0; j < i; ++j) {
            EnumFacing side = aenumfacing[j];
            BlockPos neighbour = pos.offset(side);

            if (BlockUtill.mc.world.getBlockState(neighbour).getBlock().canCollideCheck(BlockUtill.mc.world.getBlockState(neighbour), false) && !BlockUtill.mc.world.getBlockState(neighbour).getMaterial().isReplaceable()) {
                facings.add(side);
            }
        }

        return facings;
    }

    public static EnumFacing getFirstFacing(BlockPos pos) {
        Iterator iterator = getPossibleSides(pos).iterator();

        if (iterator.hasNext()) {
            EnumFacing facing = (EnumFacing) iterator.next();

            return facing;
        } else {
            return null;
        }
    }

    public static EnumFacing getRayTraceFacing(BlockPos pos) {
        RayTraceResult result = BlockUtill.mc.world.rayTraceBlocks(new Vec3d(BlockUtill.mc.player.posX, BlockUtill.mc.player.posY + (double) BlockUtill.mc.player.getEyeHeight(), BlockUtill.mc.player.posZ), new Vec3d((double) pos.getX() + 0.5D, (double) pos.getX() - 0.5D, (double) pos.getX() + 0.5D));

        return result != null && result.sideHit != null ? result.sideHit : EnumFacing.UP;
    }

    public static int isPositionPlaceable(BlockPos pos, boolean rayTrace) {
        return isPositionPlaceable(pos, rayTrace, true);
    }

    public static int isPositionPlaceable(BlockPos pos, boolean rayTrace, boolean entityCheck) {
        Block block = BlockUtill.mc.world.getBlockState(pos).getBlock();

        if (!(block instanceof BlockAir) && !(block instanceof BlockLiquid) && !(block instanceof BlockTallGrass) && !(block instanceof BlockFire) && !(block instanceof BlockDeadBush) && !(block instanceof BlockSnow)) {
            return 0;
        } else if (!rayTracePlaceCheck(pos, rayTrace, 0.0F)) {
            return -1;
        } else {
            Iterator iterator;

            if (entityCheck) {
                iterator = BlockUtill.mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(pos)).iterator();

                while (iterator.hasNext()) {
                    Entity side = (Entity) iterator.next();

                    if (!(side instanceof EntityItem) && !(side instanceof EntityXPOrb)) {
                        return 1;
                    }
                }
            }

            iterator = getPossibleSides(pos).iterator();

            EnumFacing side1;

            do {
                if (!iterator.hasNext()) {
                    return 2;
                }

                side1 = (EnumFacing) iterator.next();
            } while (!canBeClicked(pos.offset(side1)));

            return 3;
        }
    }

    public static void rightClickBlock(BlockPos pos, Vec3d vec, EnumHand hand, EnumFacing direction, boolean packet) {
        if (packet) {
            float f = (float) (vec.x - (double) pos.getX());
            float f1 = (float) (vec.y - (double) pos.getY());
            float f2 = (float) (vec.z - (double) pos.getZ());

            BlockUtill.mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(pos, direction, hand, f, f1, f2));
        } else {
            BlockUtill.mc.playerController.processRightClickBlock(BlockUtill.mc.player, BlockUtill.mc.world, pos, direction, vec, hand);
        }

        BlockUtill.mc.player.swingArm(EnumHand.MAIN_HAND);
        BlockUtill.mc.rightClickDelayTimer = 4;
    }

    public static void rightClickBlockLegit(BlockPos pos, float range, boolean rotate, EnumHand hand, AtomicDouble Yaw, AtomicDouble Pitch, AtomicBoolean rotating) {
        Vec3d eyesPos = RotationUtil.getEyesPos();
        Vec3d posVec = (new Vec3d(pos)).add(0.5D, 0.5D, 0.5D);
        double distanceSqPosVec = eyesPos.squareDistanceTo(posVec);
        EnumFacing[] aenumfacing = EnumFacing.values();
        int i = aenumfacing.length;

        for (int j = 0; j < i; ++j) {
            EnumFacing side = aenumfacing[j];
            Vec3d hitVec = posVec.add((new Vec3d(side.getDirectionVec())).scale(0.5D));
            double distanceSqHitVec = eyesPos.squareDistanceTo(hitVec);

            if (distanceSqHitVec <= MathUtil.square((double) range) && distanceSqHitVec < distanceSqPosVec && BlockUtill.mc.world.rayTraceBlocks(eyesPos, hitVec, false, true, false) == null) {
                if (rotate) {
                    float[] rotations = RotationUtil.getLegitRotations(hitVec);

                    Yaw.set((double) rotations[0]);
                    Pitch.set((double) rotations[1]);
                    rotating.set(true);
                }

                BlockUtill.mc.playerController.processRightClickBlock(BlockUtill.mc.player, BlockUtill.mc.world, pos, side, hitVec, hand);
                BlockUtill.mc.player.swingArm(hand);
                BlockUtill.mc.rightClickDelayTimer = 4;
                break;
            }
        }

    }

    public static boolean placeBlock(BlockPos pos, EnumHand hand, boolean rotate, boolean packet, boolean isSneaking) {
        boolean sneaking = false;
        EnumFacing side = getFirstFacing(pos);

        if (side == null) {
            return isSneaking;
        } else {
            BlockPos neighbour = pos.offset(side);
            EnumFacing opposite = side.getOpposite();
            Vec3d hitVec = (new Vec3d(neighbour)).add(0.5D, 0.5D, 0.5D).add((new Vec3d(opposite.getDirectionVec())).scale(0.5D));
            Block neighbourBlock = BlockUtill.mc.world.getBlockState(neighbour).getBlock();

            if (!BlockUtill.mc.player.isSneaking() && (BlockUtill.blackList.contains(neighbourBlock) || BlockUtill.shulkerList.contains(neighbourBlock))) {
                BlockUtill.mc.player.connection.sendPacket(new CPacketEntityAction(BlockUtill.mc.player, Action.START_SNEAKING));
                BlockUtill.mc.player.setSneaking(true);
                sneaking = true;
            }

            if (rotate) {
                RotationUtil.faceVector(hitVec, true);
            }

            rightClickBlock(neighbour, hitVec, hand, opposite, packet);
            BlockUtill.mc.player.swingArm(EnumHand.MAIN_HAND);
            BlockUtill.mc.rightClickDelayTimer = 4;
            return sneaking || isSneaking;
        }
    }

    public static boolean placeBlockSmartRotate(BlockPos pos, EnumHand hand, boolean rotate, boolean packet, boolean isSneaking) {
        boolean sneaking = false;
        EnumFacing side = getFirstFacing(pos);

        Command.sendMessage(side.toString());
        if (side == null) {
            return isSneaking;
        } else {
            BlockPos neighbour = pos.offset(side);
            EnumFacing opposite = side.getOpposite();
            Vec3d hitVec = (new Vec3d(neighbour)).add(0.5D, 0.5D, 0.5D).add((new Vec3d(opposite.getDirectionVec())).scale(0.5D));
            Block neighbourBlock = BlockUtill.mc.world.getBlockState(neighbour).getBlock();

            if (!BlockUtill.mc.player.isSneaking() && (BlockUtill.blackList.contains(neighbourBlock) || BlockUtill.shulkerList.contains(neighbourBlock))) {
                BlockUtill.mc.player.connection.sendPacket(new CPacketEntityAction(BlockUtill.mc.player, Action.START_SNEAKING));
                sneaking = true;
            }

            if (rotate) {
                OyVey.rotationManager.lookAtVec3d(hitVec);
            }

            rightClickBlock(neighbour, hitVec, hand, opposite, packet);
            BlockUtill.mc.player.swingArm(EnumHand.MAIN_HAND);
            BlockUtill.mc.rightClickDelayTimer = 4;
            return sneaking || isSneaking;
        }
    }

    public static void placeBlockStopSneaking(BlockPos pos, EnumHand hand, boolean rotate, boolean packet, boolean isSneaking) {
        boolean sneaking = placeBlockSmartRotate(pos, hand, rotate, packet, isSneaking);

        if (!isSneaking && sneaking) {
            BlockUtill.mc.player.connection.sendPacket(new CPacketEntityAction(BlockUtill.mc.player, Action.STOP_SNEAKING));
        }

    }

    public static Vec3d[] getHelpingBlocks(Vec3d vec3d) {
        return new Vec3d[] { new Vec3d(vec3d.x, vec3d.y - 1.0D, vec3d.z), new Vec3d(vec3d.x != 0.0D ? vec3d.x * 2.0D : vec3d.x, vec3d.y, vec3d.x != 0.0D ? vec3d.z : vec3d.z * 2.0D), new Vec3d(vec3d.x == 0.0D ? vec3d.x + 1.0D : vec3d.x, vec3d.y, vec3d.x == 0.0D ? vec3d.z : vec3d.z + 1.0D), new Vec3d(vec3d.x == 0.0D ? vec3d.x - 1.0D : vec3d.x, vec3d.y, vec3d.x == 0.0D ? vec3d.z : vec3d.z - 1.0D), new Vec3d(vec3d.x, vec3d.y + 1.0D, vec3d.z)};
    }

    public static List possiblePlacePositions(float placeRange) {
        NonNullList positions = NonNullList.create();

        positions.addAll((Collection) getSphere(EntityUtil.getPlayerPos(BlockUtill.mc.player), placeRange, (int) placeRange, false, true, 0).stream().filter(BlockUtill::canPlaceCrystal).collect(Collectors.toList()));
        return positions;
    }

    public static List getSphere(BlockPos pos, float r, int h, boolean hollow, boolean sphere, int plus_y) {
        ArrayList circleblocks = new ArrayList();
        int cx = pos.getX();
        int cy = pos.getY();
        int cz = pos.getZ();

        for (int x = cx - (int) r; (float) x <= (float) cx + r; ++x) {
            int z = cz - (int) r;

            while ((float) z <= (float) cz + r) {
                int y = sphere ? cy - (int) r : cy;

                while (true) {
                    float f = (float) y;
                    float f2 = sphere ? (float) cy + r : (float) (cy + h);

                    if (f >= f2) {
                        ++z;
                        break;
                    }

                    double dist = (double) ((cx - x) * (cx - x) + (cz - z) * (cz - z) + (sphere ? (cy - y) * (cy - y) : 0));

                    if (dist < (double) (r * r) && (!hollow || dist >= (double) ((r - 1.0F) * (r - 1.0F)))) {
                        BlockPos l = new BlockPos(x, y + plus_y, z);

                        circleblocks.add(l);
                    }

                    ++y;
                }
            }
        }

        return circleblocks;
    }

    public static boolean canPlaceCrystal(BlockPos blockPos) {
        BlockPos boost = blockPos.add(0, 1, 0);
        BlockPos boost2 = blockPos.add(0, 2, 0);

        try {
            return (BlockUtill.mc.world.getBlockState(blockPos).getBlock() == Blocks.BEDROCK || BlockUtill.mc.world.getBlockState(blockPos).getBlock() == Blocks.OBSIDIAN) && BlockUtill.mc.world.getBlockState(boost).getBlock() == Blocks.AIR && BlockUtill.mc.world.getBlockState(boost2).getBlock() == Blocks.AIR && BlockUtill.mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(boost)).isEmpty() && BlockUtill.mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(boost2)).isEmpty();
        } catch (Exception exception) {
            return false;
        }
    }

    public static List possiblePlacePositions(float placeRange, boolean specialEntityCheck, boolean oneDot15) {
        NonNullList positions = NonNullList.create();

        positions.addAll((Collection) getSphere(EntityUtil.getPlayerPos(BlockUtill.mc.player), placeRange, (int) placeRange, false, true, 0).stream().filter((pos) -> {
            return canPlaceCrystal(pos, specialEntityCheck, oneDot15);
        }).collect(Collectors.toList()));
        return positions;
    }

    public static boolean canPlaceCrystal(BlockPos blockPos, boolean specialEntityCheck, boolean oneDot15) {
        BlockPos boost = blockPos.add(0, 1, 0);
        BlockPos boost2 = blockPos.add(0, 2, 0);

        try {
            if (BlockUtill.mc.world.getBlockState(blockPos).getBlock() != Blocks.BEDROCK && BlockUtill.mc.world.getBlockState(blockPos).getBlock() != Blocks.OBSIDIAN) {
                return false;
            } else if ((oneDot15 || BlockUtill.mc.world.getBlockState(boost2).getBlock() == Blocks.AIR) && BlockUtill.mc.world.getBlockState(boost).getBlock() == Blocks.AIR) {
                Iterator ignored = BlockUtill.mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(boost)).iterator();

                Entity entity;

                do {
                    do {
                        if (!ignored.hasNext()) {
                            if (!oneDot15) {
                                ignored = BlockUtill.mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(boost2)).iterator();

                                while (ignored.hasNext()) {
                                    entity = (Entity) ignored.next();
                                    if (!entity.isDead && (!specialEntityCheck || !(entity instanceof EntityEnderCrystal))) {
                                        return false;
                                    }
                                }
                            }

                            return true;
                        }

                        entity = (Entity) ignored.next();
                    } while (entity.isDead);
                } while (specialEntityCheck && entity instanceof EntityEnderCrystal);

                return false;
            } else {
                return false;
            }
        } catch (Exception exception) {
            return false;
        }
    }

    public static boolean canBeClicked(BlockPos pos) {
        return getBlock(pos).canCollideCheck(getState(pos), false);
    }

    private static Block getBlock(BlockPos pos) {
        return getState(pos).getBlock();
    }

    private static IBlockState getState(BlockPos pos) {
        return BlockUtill.mc.world.getBlockState(pos);
    }

    public static boolean isBlockAboveEntitySolid(Entity entity) {
        if (entity != null) {
            BlockPos pos = new BlockPos(entity.posX, entity.posY + 2.0D, entity.posZ);

            return isBlockSolid(pos);
        } else {
            return false;
        }
    }

    public static void debugPos(String message, BlockPos pos) {
        Command.sendMessage(message + pos.getX() + "x, " + pos.getY() + "y, " + pos.getZ() + "z");
    }

    public static void placeCrystalOnBlock(BlockPos pos, EnumHand hand, boolean swing, boolean exactHand) {
        RayTraceResult result = BlockUtill.mc.world.rayTraceBlocks(new Vec3d(BlockUtill.mc.player.posX, BlockUtill.mc.player.posY + (double) BlockUtill.mc.player.getEyeHeight(), BlockUtill.mc.player.posZ), new Vec3d((double) pos.getX() + 0.5D, (double) pos.getY() - 0.5D, (double) pos.getZ() + 0.5D));
        EnumFacing facing = result != null && result.sideHit != null ? result.sideHit : EnumFacing.UP;

        BlockUtill.mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(pos, facing, hand, 0.0F, 0.0F, 0.0F));
        if (swing) {
            BlockUtill.mc.player.connection.sendPacket(new CPacketAnimation(exactHand ? hand : EnumHand.MAIN_HAND));
        }

    }

    public static BlockPos[] toBlockPos(Vec3d[] vec3ds) {
        BlockPos[] list = new BlockPos[vec3ds.length];

        for (int i = 0; i < vec3ds.length; ++i) {
            list[i] = new BlockPos(vec3ds[i]);
        }

        return list;
    }

    public static Vec3d posToVec3d(BlockPos pos) {
        return new Vec3d(pos);
    }

    public static BlockPos vec3dToPos(Vec3d vec3d) {
        return new BlockPos(vec3d);
    }

    public static Boolean isPosInFov(BlockPos pos) {
        int dirnumber = RotationUtil.getDirection4D();

        return dirnumber == 0 && (double) pos.getZ() - BlockUtill.mc.player.getPositionVector().z < 0.0D ? Boolean.valueOf(false) : (dirnumber == 1 && (double) pos.getX() - BlockUtill.mc.player.getPositionVector().x > 0.0D ? Boolean.valueOf(false) : (dirnumber == 2 && (double) pos.getZ() - BlockUtill.mc.player.getPositionVector().z > 0.0D ? Boolean.valueOf(false) : Boolean.valueOf(dirnumber != 3 || (double) pos.getX() - BlockUtill.mc.player.getPositionVector().x >= 0.0D)));
    }

    public static boolean isBlockBelowEntitySolid(Entity entity) {
        if (entity != null) {
            BlockPos pos = new BlockPos(entity.posX, entity.posY - 1.0D, entity.posZ);

            return isBlockSolid(pos);
        } else {
            return false;
        }
    }

    public static boolean isBlockSolid(BlockPos pos) {
        return !isBlockUnSolid(pos);
    }

    public static boolean isBlockUnSolid(BlockPos pos) {
        return isBlockUnSolid(BlockUtill.mc.world.getBlockState(pos).getBlock());
    }

    public static boolean isBlockUnSolid(Block block) {
        return BlockUtill.unSolidBlocks.contains(block);
    }

    public static boolean isBlockUnSafe(Block block) {
        return BlockUtill.unSafeBlocks.contains(block);
    }

    public static Vec3d[] convertVec3ds(Vec3d vec3d, Vec3d[] input) {
        Vec3d[] output = new Vec3d[input.length];

        for (int i = 0; i < input.length; ++i) {
            output[i] = vec3d.add(input[i]);
        }

        return output;
    }

    public static Vec3d[] convertVec3ds(EntityPlayer entity, Vec3d[] input) {
        return convertVec3ds(entity.getPositionVector(), input);
    }

    public static boolean canBreak(BlockPos pos) {
        IBlockState blockState = BlockUtill.mc.world.getBlockState(pos);
        Block block = blockState.getBlock();

        return block.getBlockHardness(blockState, BlockUtill.mc.world, pos) != -1.0F;
    }

    public static boolean isValidBlock(BlockPos pos) {
        Block block = BlockUtill.mc.world.getBlockState(pos).getBlock();

        return !(block instanceof BlockLiquid) && block.getMaterial((IBlockState) null) != Material.AIR;
    }

    public static boolean isScaffoldPos(BlockPos pos) {
        return BlockUtill.mc.world.isAirBlock(pos) || BlockUtill.mc.world.getBlockState(pos).getBlock() == Blocks.SNOW_LAYER || BlockUtill.mc.world.getBlockState(pos).getBlock() == Blocks.TALLGRASS || BlockUtill.mc.world.getBlockState(pos).getBlock() instanceof BlockLiquid;
    }

    public static boolean rayTracePlaceCheck(BlockPos pos, boolean shouldCheck, float height) {
        return !shouldCheck || BlockUtill.mc.world.rayTraceBlocks(new Vec3d(BlockUtill.mc.player.posX, BlockUtill.mc.player.posY + (double) BlockUtill.mc.player.getEyeHeight(), BlockUtill.mc.player.posZ), new Vec3d((double) pos.getX(), (double) ((float) pos.getY() + height), (double) pos.getZ()), false, true, false) == null;
    }

    public static boolean rayTracePlaceCheck(BlockPos pos, boolean shouldCheck) {
        return rayTracePlaceCheck(pos, shouldCheck, 1.0F);
    }

    public static boolean rayTracePlaceCheck(BlockPos pos) {
        return rayTracePlaceCheck(pos, true);
    }
}
