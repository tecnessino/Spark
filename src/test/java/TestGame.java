import org.lwjgl.util.vector.Vector3f;
import org.spark.Game;
import org.spark.actor.Actor;
import org.spark.actor.ActorModule;
import org.spark.light.Light;
import org.spark.material.Material;
import org.spark.math.MatrixUtil;
import org.spark.mesh.MeshRender;
import org.spark.skybox.Skybox;
import org.spark.texture.Texture;

import java.awt.*;

public class TestGame extends Game {
    public Actor actor;
    @Override
    public void onStart() {
        super.onStart();

        Actor skybox = CreateActor();
        skybox.addModule(new Skybox("/Demo/Skybox/",100));

        Material material = new Material();
        material.setColor(new Color(255, 0, 0));
        material.setUseColor(true);
        //material.disableReactionToLight();
        material.secret();
        material.setCubeMapReflections(true);

        Light light = CreateLight();
        light.position = new Vector3f(0,-0.1f,-3);

        actor = CreateActor();
        actor.setPosition(new Vector3f(0,-0.3f,-3));
        actor.addModule(new MeshRender(getMeshLoader().loadObjModel("/Demo/dragon.obj"),material));

        actor.getScale().x = 0.1f;
        actor.getPosition().y = -0.6f;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        actor.getRotation().y += 0.01f;
    }

}
