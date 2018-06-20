package de.hgv.view

import de.hgv.model.Phase
import de.hgv.model.Spiel
import de.hgv.provider.ActiveProvider

class Finale(val saison:Int) {
    val finalspiele: List<Spiel> = ActiveProvider.getSpieleInPhase(Phase.FINALE, saison){}
    fun getPaarung(a: Int): Spiel{
        return finalspiele[a]
    }
}