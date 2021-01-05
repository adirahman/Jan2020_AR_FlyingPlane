package com.androiddevs.flyingplane

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.ar.core.Anchor
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.Node
import com.google.ar.sceneform.math.Quaternion
import com.google.ar.sceneform.math.Vector3
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.ux.ArFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var arFragment: ArFragment
    private val modelResourceIds = arrayOf(
        R.raw.star_destroyer,
        R.raw.tie_silencer,
        R.raw.xwing
    )

    private var curCameraPosition = Vector3.zero()
    private val nodes = mutableListOf<RotatingNode>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        arFragment = fragment as ArFragment

        arFragment.setOnTapArPlaneListener{ hitResult, _, _ ->
            val randomId = modelResourceIds.random()
            loadModelAddToScene(hitResult.createAnchor(), randomId)
        }

        arFragment.arSceneView.scene.addOnUpdateListener {
            updateNodes()
        }
    }

    private fun updateNodes(){
        curCameraPosition = arFragment.arSceneView.scene.camera.worldPosition
        for(node in nodes){
            node.worldPosition = Vector3(curCameraPosition.x, node.worldPosition.y, curCameraPosition.z)
        }
    }

    private fun loadModelAddToScene(anchor: Anchor, modelResourceId:Int){
        ModelRenderable.builder()
            .setSource(this, modelResourceId)
            .build()
            .thenAccept { modelRenderable ->
                  val spaceShip = when(modelResourceId){
                      R.raw.star_destroyer -> SpaceShip.StarDestroyer
                      R.raw.tie_silencer -> SpaceShip.TieSilencer
                      R.raw.xwing -> SpaceShip.XWing
                      else -> SpaceShip.XWing
                  }
                addNodeToScene(anchor, modelRenderable, spaceShip)
            }.exceptionally {
                Toast.makeText(this,"Error creating node: $it",Toast.LENGTH_LONG).show()
                null
            }
    }

    private fun addNodeToScene(anchor: Anchor, modelRenderable: ModelRenderable,spaceShip: SpaceShip){
        val anchorNode = AnchorNode(anchor)
        val rotatingNode = RotatingNode(spaceShip.degreesPerSecond).apply {
            setParent(anchorNode)
        }
        Node().apply {
            renderable = modelRenderable
            setParent(rotatingNode)
            localPosition = Vector3(spaceShip.radius, spaceShip.height, 0f)
            localRotation = Quaternion.eulerAngles(Vector3(0f,spaceShip.rotationDegress,0f))
        }
        arFragment.arSceneView.scene.addChild(anchorNode)
        nodes.add(rotatingNode)
    }
}