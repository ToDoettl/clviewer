package de.hgv.view

import de.hgv.model.Phase
import de.hgv.model.Verein
import de.hgv.provider.ActiveProvider
import javafx.beans.property.SimpleIntegerProperty
import javafx.geometry.Pos
import javafx.scene.image.Image
import javafx.scene.text.Font
import javafx.scene.text.FontWeight
import tornadofx.*
import java.time.LocalDate


class TurnierbaumView : Fragment() {
    val saison = params["saison"] as? Int ?: LocalDate.now().let {
        if (it.isBefore(LocalDate.of(it.year, 7, 1))) {
            it.year
        } else {
            it.year + 1
        }
    }
    val saisonProptery = SimpleIntegerProperty(saison)
    val turnier = KoSpiele(saison).getTurnierbaum()
    val vereine = getVereineinPhase(Phase.ACHTELFINALE)
    //val wappen: Map<Verein?, Image> = downloadWappen(vereine)
    //TODO Akutelle Saison statt 2018 verwnden
    val jahre = (2018 downTo 2004).toList().observable()


    val aktuellePhase: Phase? =
            ActiveProvider
                    .getSpiele(saison)
                    .filterNot { it.toString().contains("gruppe", true) }
                    .map { it.phase }
                    .distinct()
                    .maxBy { it.ordinal }

    fun getVereineinPhase(phase: Phase): List<Verein> {
        val spiele = ActiveProvider.getSpieleInPhase(phase, saison) {}
        val vereine = mutableListOf<Verein>()

        for (i in spiele) {
            vereine.add(i.daheim)
        }
        vereine.distinct()

        return vereine
    }


    override val root = pane {
        borderpane {
            runAsyncWithOverlay {
                downloadWappen(vereine)
            } ui { wappen: Map<Verein?, Image> ->
                top = vbox {
                    label("Turnierbaum") {
                        useMaxWidth = true
                        alignment = Pos.CENTER
                        font = Font.font(font.family, FontWeight.BOLD, 30.0)
                    }
                    hbox {
                        alignment = Pos.CENTER
                        label("Saison:  ")

                        combobox(saisonProptery, jahre) {
                            cellFormat {
                                text = "${item.toInt() - 1}/$item"
                            }
                        }
                    }
                }

                center = buildTurnierbaum(saison)
            }
        }
    }

    init {
        primaryStage.isMaximized = true

        saisonProptery.onChange { neu ->
            val turnierbaumNeu = find<TurnierbaumView>(mapOf("saison" to neu))
            this.replaceWith(turnierbaumNeu, ViewTransition.Fade(15.millis))
        }
    }

}









