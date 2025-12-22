package io.amogus.managers;

public final class Managers {
    public static AnimationManager am;
    public static EntityManager em;
    public static LevelManager lm;
    public static ParticleManager pm;
    public static ServerManager svm;
    public static SpriteManager sm;
    public static ViewportManager vm;
    public static TextureManager tm;

    public static void init() {
        tm = TextureManager.getInstance();
        sm = SpriteManager.getInstance();
        vm = ViewportManager.getInstance();
        lm = LevelManager.getInstance();
        pm = ParticleManager.getInstance();
        am = AnimationManager.getInstance();
        svm = ServerManager.getInstance();
        em = EntityManager.getInstance();
    }

    private Managers() {}
}
