package com.noobanidus.dwmh.capability;

import net.minecraft.nbt.NBTTagString;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.UUID;

public class CapabilityOwner implements INBTSerializable<NBTTagString> {
    private UUID owner = null;

    public UUID getOwner() {
        return owner;
    }

    public void setOwner(UUID newOwner) {
        owner = newOwner;
    }

    public boolean hasOwner() {
        return owner != null;
    }

    public boolean ownerMatches(UUID otherOwner) {
        return owner.equals(otherOwner);
    }

    @Override
    public NBTTagString serializeNBT() {
        String ownerString = "";

        if (owner != null) {
            ownerString = owner.toString();
        }

        return new NBTTagString(ownerString);
    }

    @Override
    public void deserializeNBT(NBTTagString nbt) {
        String ownerString = nbt.getString();

        if (ownerString.isEmpty()) {
            setOwner(null);
        } else {
            setOwner(UUID.fromString(ownerString));
        }
    }
}
