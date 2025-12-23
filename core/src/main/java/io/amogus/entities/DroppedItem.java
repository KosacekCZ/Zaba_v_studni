package io.amogus.entities;

public class DroppedItem extends Entity {
    protected float t;

    public DroppedItem(float x, float y, String texture) {
        super(x, y, texture);
    }

    @Override
    public void updateWorld() {
        t = (t < 60? ++t : 0);
        sm.draw(x, (float)(y + Math.sin(t / 10) * 2f), 32, 32, texture);
    }

    @Override
    public void updateScreen() {

    }

    @Override
    public void onCollide(Entity e) {

    }
}
