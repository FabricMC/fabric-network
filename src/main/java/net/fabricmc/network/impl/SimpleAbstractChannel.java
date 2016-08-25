package net.fabricmc.network.impl;

import net.fabricmc.api.Side;
import net.fabricmc.api.Sided;
import net.fabricmc.base.Fabric;
import net.fabricmc.network.AbstractChannel;
import net.fabricmc.network.AbstractPacket;
import net.fabricmc.network.NetworkManager;
import net.fabricmc.network.util.NetworkHelper;
import net.minecraft.client.player.EntityPlayerClient;
import net.minecraft.entity.player.EntityPlayerServer;
import net.minecraft.network.packet.client.CPacketCustomPayload;
import net.minecraft.network.packet.server.SPacketCustomPayload;
import net.minecraft.util.PacketByteBuf;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.WorldServer;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public abstract class SimpleAbstractChannel<T extends AbstractPacket> extends AbstractChannel<T> {
    protected PacketByteBuf createPacketBuf(T packet) {
        return NetworkHelper.createPacketBuf(this, packet);
    }

    protected String getChannel() {
        return NetworkManager.CHANNEL_PREFIX + NetworkManager.getChannelName(this);
    }

    @Sided(Side.CLIENT)
    public void sendToServer(T packet) {
        ((EntityPlayerClient) Fabric.getSidedHandler().getClientPlayer()).networkHandler.sendPacket(new SPacketCustomPayload(getChannel(), createPacketBuf(packet)));
    }

    public void sendToPlayer(T packet, EntityPlayerServer player) {
        player.networkHandler.sendPacket(new CPacketCustomPayload(getChannel(), createPacketBuf(packet)));
    }

    public void sendToAll(T packet, List<EntityPlayerServer> players) {
        CPacketCustomPayload wrapper = new CPacketCustomPayload(getChannel(), createPacketBuf(packet));
        players.forEach(player -> player.networkHandler.sendPacket(wrapper));
    }

    public void sendToAll(T packet, List<EntityPlayerServer> players, Predicate<EntityPlayerServer> predicate) {
        sendToAll(packet, players.stream()
                .filter(predicate)
                .collect(Collectors.toList()));
    }

    public void sendToAllInRadius(T packet, WorldServer world, Vec3d pos, double radius) {
        double maxDist = radius * radius + radius * radius + radius * radius;
        sendToAll(packet, world.b(EntityPlayerServer.class,
                player -> player.getDistanceTo(pos.x, pos.y, pos.z) <= maxDist));
    }

    public void sendToAllInRadius(T packet, WorldServer world, Vec3i pos, double radius) {
        sendToAllInRadius(packet, world, new Vec3d(pos.x + 0.5, pos.y + 0.5, pos.z + 0.5), radius);
    }

    public void sendToAllInRadius(T packet, int dimension, Vec3d pos, double radius) {
        sendToAllInRadius(packet, Fabric.getSidedHandler().getServerInstance().getWorld(dimension), pos, radius);
    }

    public void sendToAllInRadius(T packet, int dimension, Vec3i pos, double radius) {
        sendToAllInRadius(packet, Fabric.getSidedHandler().getServerInstance().getWorld(dimension), pos, radius);
    }
}
