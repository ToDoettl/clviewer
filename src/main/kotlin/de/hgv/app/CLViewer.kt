package de.hgv.app

import de.hgv.view.TurnierbaumView
import javafx.application.Application
import tornadofx.*

class CLViewer: App(TurnierbaumView::class)

fun main(args: Array<String>) {
    Application.launch(CLViewer::class.java)
}