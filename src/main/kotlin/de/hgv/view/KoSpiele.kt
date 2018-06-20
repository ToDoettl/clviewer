package de.hgv.view

import de.hgv.model.Phase
import de.hgv.model.Spiel
import de.hgv.model.Verein
import de.hgv.provider.ActiveProvider
import tornadofx.swap

class KoSpiele(val saison:Int) {
    val allespiele = ActiveProvider.getSpiele(saison).groupBy { it.phase }
    private val turnierbaum = mutableMapOf<Phase, List<List<Spiel>>>()

    fun getTurnierbaum(): Map<Phase, List<List<Spiel>>> {
        getTurnierbaum(Phase.ACHTELFINALE, mutableListOf())

        return turnierbaum
    }

    //rekursive Methode, die eine MutableList des Tunierbaums zurückgibt
    private fun getTurnierbaum(phase: Phase?, letztePhase: MutableList<List<Spiel>>): MutableList<List<Spiel>> {

        if (phase == null) {
            return letztePhase
        }

        val spiele = allespiele[phase] ?: return letztePhase

        //Spiele in Paarungen aufteilen
        var paarungen = mutableListOf<List<Spiel>>()
        for (spiel in spiele) {
            if (paarungen.any { spiel in it }) {
                continue
            }

            val rueckspiel = spiele.find { it.daheim == spiel.auswaerts }
            paarungen.add(listOfNotNull(spiel, rueckspiel))
        }

        if (paarungen.isNotEmpty()) {
            //rekursiver Aufruf:
            paarungen = getTurnierbaum(phase.naechste(), paarungen)
        }

        //Vertauschen der Spiele, damit die Spiele in der korrekten Reihenfolge angezeigt werden
        if (letztePhase.isNotEmpty()) {
            paarungen.forEachIndexed { index, paarung ->
                val vereine = paarung[0].daheim to paarung[0].auswaerts

                letztePhase.swap(2 * index, letztePhase.indexOfFirst { it.any { it.daheim == vereine.first } })
                letztePhase.swap(2 * index + 1, letztePhase.indexOfFirst { it.any { it.daheim == vereine.second } })
            }
        }
        turnierbaum[phase] = paarungen

        return letztePhase
    }




    fun listeSortieren(c: List<Spiel>): MutableList<Spiel>{
        var bessereListe: MutableList<Spiel> = mutableListOf()
        for (spiel: Spiel in c){
            bessereListe.add(spiel)
        }
        for (i: Int in (1..bessereListe.size-1) step 2){
            val mannschaft = bessereListe[i-1].auswaerts.name
            val ind: Int = (bessereListe.search(mannschaft))
            bessereListe.swap(ind, i)
        }
        for (j: Int in (1..bessereListe.size-1)step 2){
            if (bessereListe[j].datum.isBefore(bessereListe[j-1].datum)){
                bessereListe.swap(j, j-1)
            }
        }
        return bessereListe
    }


    /*fun getBeteiligungIndex(verein: Verein):Int{
        var index = 0
        for (spiel: Spiel in bessereListe){
            if (spiel.daheim == verein || spiel.auswaerts == verein){
                break
            }
            else
            {
               index++
            }
        }
        return index
    }*/
}

/*fun MutableList<Spiel>.search(b: String): Int{
    var index = 0
    for (spiel: Spiel in this){
        if (spiel.daheim.name  == b){
            break
        }
        else
        {
            index++
        }

    }
    return index
}

fun MutableList<Spiel>.swap(index1: Int, index2: Int){
    val tmp: Spiel = this[index1]
    this[index1] = this [index2]
    this[index2] = tmp
}*/


