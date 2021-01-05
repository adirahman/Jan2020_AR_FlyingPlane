package com.androiddevs.flyingplane

sealed class SpaceShip {

    abstract val degreesPerSecond : Float
    abstract val radius: Float
    abstract val height: Float
    abstract val rotationDegress: Float

    object StarDestroyer : SpaceShip(){
        override val degreesPerSecond: Float
            get() = 20f
        override val radius: Float
            get() = 2f
        override val height: Float
            get() = 0.7f
        override val rotationDegress: Float
            get() = 180f
    }

    object TieSilencer : SpaceShip(){
        override val degreesPerSecond: Float
            get() = 90f
        override val radius: Float
            get() = 1.5f
        override val height: Float
            get() = 1f
        override val rotationDegress: Float
            get() = 90f
    }

    object XWing : SpaceShip(){
        override val degreesPerSecond: Float
            get() = 70f
        override val radius: Float
            get() = 1.2f
        override val height: Float
            get() = 0.5f
        override val rotationDegress: Float
            get() = -90f

    }
}