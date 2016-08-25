/*
 * Copyright 2016 FabricMC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.fabricmc.network.mixin;

import net.fabricmc.network.test.NetworkTest;
import net.fabricmc.network.test.TestAsyncPacket;
import net.fabricmc.network.test.TestPacket;
import net.minecraft.block.IBlockState;
import net.minecraft.block.impl.BlockDirt;
import net.minecraft.client.world.WorldClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Facing;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = BlockDirt.class, remap = false)
public abstract class TestMixin {


	public boolean activate(World world, BlockPos pos, IBlockState state, EntityPlayer player, Hand hand, Facing facing, float hitX, float hitY, float hitZ) {
		if (world.isRemote) {
			NetworkTest.channel.sendToServer(new TestPacket(42));
			NetworkTest.channel.sendToServer(new TestAsyncPacket(69));
		}
		return true;
	}


}
