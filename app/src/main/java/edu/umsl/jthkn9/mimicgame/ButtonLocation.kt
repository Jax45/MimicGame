package edu.umsl.jthkn9.mimicgame

import java.util.*

enum class ButtonLocation {
    TOPLEFT,
    TOPRIGHT,
    BOTTOMLEFT,
    BOTTOMRIGHT;

    public fun getRandomButtonLocation(): ButtonLocation? {
        val random = Random()
        return values()[random.nextInt(values().size)]
    }
}