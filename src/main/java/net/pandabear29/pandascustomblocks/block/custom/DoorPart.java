package net.pandabear29.pandascustomblocks.block.custom;

import net.minecraft.util.StringRepresentable;

public enum DoorPart implements StringRepresentable {
    BOTTOM_LEFT("bottom_left", 0, 0),
    BOTTOM_RIGHT("bottom_right", 1, 0),
    LOWER_MIDDLE_LEFT("lower_middle_left", 0, 1),
    LOWER_MIDDLE_RIGHT("lower_middle_right", 1, 1),
    UPPER_MIDDLE_LEFT("upper_middle_left", 0, 2),
    UPPER_MIDDLE_RIGHT("upper_middle_right", 1, 2),
    TOP_LEFT("top_left", 0, 3),
    TOP_RIGHT("top_right", 1, 3);

    private final String name;
    private final int x;
    private final int y;

    DoorPart(String name, int x, int y) {
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

    public static DoorPart from(int x, int y) {
        for(DoorPart part : values()) {
            if(part.getX() == x && part.getY() == y) {
                return part;
            }
        }
        return BOTTOM_LEFT; // Default
    }
}