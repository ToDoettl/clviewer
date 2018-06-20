package de.hgv.view

import de.hgv.model.Phase
import de.hgv.model.Spiel
import de.hgv.provider.ActiveProvider

class Achtelfinale(val saison:Int) {
    val achtelfinalspiele: List<Spiel> = ActiveProvider.getSpieleInPhase(Phase.ACHTELFINALE, saison){}



    fun listeSortieren(): MutableList<Spiel>{
        var bessereListe: MutableList<Spiel> = mutableListOf()
        for (spiel: Spiel in achtelfinalspiele){
            bessereListe.add(spiel)
        }
        for (i: Int in (0..bessereListe.size-1) step 2){
            val mannschaft = bessereListe[i].auswaerts.name
            val ind: Int = (bessereListe.search(mannschaft))
            bessereListe.swap(ind, i+1)
        }
        for (j: Int in (0..bessereListe.size-1)step 2){
            if (bessereListe[j+1].datum.isBefore(bessereListe[j].datum)){
                bessereListe.swap(j, j+1)
            }
        }
        return bessereListe
    }




    fun getPaarung(a: Int): Spiel{
        return achtelfinalspiele[a]
    }
}

fun MutableList<Spiel>.search(b: String): Int{
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
}
