package net.pandabear29.pandascustomblocks.block.custom;

import net.minecraft.util.StringRepresentable;

public enum MediumDoorPart implements StringRepresentable {
    BOTTOM_LEFT("bottom_left", 0, 0),
    BOTTOM_RIGHT("bottom_right", 1, 0),
    MIDDLE_LEFT("middle_left", 0, 1),
    MIDDLE_RIGHT("middle_right", 1, 1),
    TOP_LEFT("top_left", 0, 2),
    TOP_RIGHT("top_right", 1, 2);

    private final String name;
    private final int x;
    private final int y;

    MediumDoorPart(String name, int x, int y) {
        this.name = name;
        this.x = x;
        this.y = y;
    }

    public String toString() {
        return this.name;
    }

    @Override
    public String getSerializedName() {
        return this.name;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public static MediumDoorPart from(int x, int y) {
        for(MediumDoorPart part : values()) {
            if(part.getX() == x && part.getY() == y) {
                return part;
            }
        }
        return BOTTOM_LEFT; // Default
    }
}