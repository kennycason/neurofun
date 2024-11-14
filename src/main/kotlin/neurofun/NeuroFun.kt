package neurofun


import neurofun.dna.DNAFarm
import neurofun.dna.Genetic
import neurofun.dna.Organism
import neurofun.dna.RandomDNAGenerator
import neurofun.image.Entropy
import neurofun.random.Dice
import neurofun.util.padWithZeros
import java.awt.Color
import java.awt.Graphics
import java.awt.event.KeyAdapter
import java.awt.event.KeyEvent
import java.awt.image.BufferedImage
import java.io.File
import java.lang.Thread.sleep
import javax.imageio.ImageIO
import javax.swing.JFrame
import javax.swing.JPanel
import javax.swing.WindowConstants

fun main(args: Array<String>) {
    NeuroFun().run()
}

class NeuroFun {
    // 16:9 512x288
    private val worldWidth = 288
    private val worldHeight = 512
    private val scale = 2
    private val canvas = BufferedImage(worldWidth, worldHeight, BufferedImage.TYPE_INT_ARGB)
    private val canvasGraphics = canvas.graphics
    private var turnsEntropyIsBelowThreshold = 0
    private var turnsSinceEntropyAboveThreshold = 0
    private var mutationRate = 0.05
    private val saveImage = true
    private val neurofunBaseFolderName = System.getProperty("user.home") + "/neurofun"
    private val neurofunRunsFolderName = "$neurofunBaseFolderName/runs"
    private val currentIterationFolder = "$neurofunRunsFolderName/${System.currentTimeMillis() / 1000}"
    private val dnaFarm = DNAFarm()
    private var organism = loadRandomOrganism()

    fun run() {
        var i = 0
        val panel = object : JPanel() {
            init {
                isFocusable = true
                requestFocusInWindow()

                addKeyListener(object : KeyAdapter() {
                    override fun keyPressed(e: KeyEvent) {
                        println("key pressed")
                        if (e.keyCode == KeyEvent.VK_ENTER) {
                            saveCanvasAsImage()
                        }
                        if (e.keyCode == KeyEvent.VK_N) {
                            println("build new random dna")
                            organism = buildRandomOrganism()
                            turnsSinceEntropyAboveThreshold = 0
                        }
                        if (e.keyCode == KeyEvent.VK_B) {
                            println("reproduce new dna from farm")
                            organism = birthNewOrganism()
                            turnsSinceEntropyAboveThreshold = 0
                        }
                        if (e.keyCode == KeyEvent.VK_R) {
                            println("load random dna from farm")
                            organism = loadRandomOrganism()
                            turnsSinceEntropyAboveThreshold = 0
                        }
                        if (e.keyCode == KeyEvent.VK_S) {
                            println("save dna")
                            dnaFarm.writeDNA(organism.dna, canvas)
                        }
                    }
                })
            }

            override fun paintComponent(g: Graphics) {
                super.paintComponent(g)

                canvasGraphics.color = Color.BLACK
                canvasGraphics.clearRect(0, 0, width, height)

                //println(organism.dna)
                organism.step()
//                ImageToGrayScaleBuffer.addToBuffer(targetImage, 0.4, organism.buffer)

                organism.express(canvasGraphics)
                organism.entropy = Entropy.calculateNormalizedEntropy(canvas)

                if (organism.entropy <= 0.01) {
                    turnsEntropyIsBelowThreshold++
                    turnsSinceEntropyAboveThreshold = 0
                } else {
                    turnsEntropyIsBelowThreshold = 0
                    turnsSinceEntropyAboveThreshold++
                }

                if (turnsEntropyIsBelowThreshold > 5) {
                    println("reset because boring")
//                    organism = birthNewOrganism()
                    organism = buildRandomOrganism()
                } else if (turnsSinceEntropyAboveThreshold >= 50) {
                    println("reset because pattern hit 50 iterations")
//                    organism = birthNewOrganism()
                    organism = buildRandomOrganism()
                    turnsSinceEntropyAboveThreshold = 0
                } else {
                    println("mutate")
                   // mutationRate = getMutationRate(organism.entropy, minRate = 0.005, maxRate = 0.01)
                    organism = Genetic.mutateOrganism(organism, mutationRate)
                }

//                println("$i, ${organism.entropy}, $mutationRate")
                g.drawImage(canvas, 0, 0, width, height, this)
                if (saveImage) saveCanvasAsImage()
                i++
                //println(organism.nn)
            }

            /*
             * normalized entropy between 0.0 and 1.0
             * Scale mutation rate based on normalized entropy
             * Higher entropy means lower mutation rate (more interesting images need less mutation)
             *
             */
            private fun getMutationRate(normalizedEntropy: Double, minRate: Double = 0.01, maxRate: Double = 1.0): Double {
                return minRate + ((1.0 - normalizedEntropy) * (maxRate - minRate) / 10.0)
            }

            private fun saveCanvasAsImage() {
                val neurofunBaseFolder = File(neurofunBaseFolderName)
                if (!neurofunBaseFolder.exists()) {
                    println("Creating neurofun base folder: $neurofunBaseFolder")
                    neurofunBaseFolder.mkdirs()
                }

                val neurofunRunsFolder = File(neurofunRunsFolderName)
                if (!neurofunRunsFolder.exists()) {
                    println("Creating neurofun folder: $neurofunRunsFolderName")
                    neurofunRunsFolder.mkdirs()
                }

                val currentIterationFolder = File(currentIterationFolder)
                if (!currentIterationFolder.exists()) {
                    println("Creating folder for current iteration: $currentIterationFolder")
                    currentIterationFolder.mkdirs()
                }

                val fileName = "$currentIterationFolder/${i.padWithZeros(5)}.png"
                ImageIO.write(canvas, "png", File(fileName))
                // println("saved image to $fileName")
            }
        }

        val frame = JFrame()
        frame.defaultCloseOperation = WindowConstants.EXIT_ON_CLOSE
        frame.setSize(worldWidth * scale, (worldHeight * scale) + 18)
        frame.isVisible = true
        frame.add(panel)
        panel.revalidate()

        while (true) {
            panel.repaint()
            sleep(20)
        }
    }

    private fun buildRandomOrganism() = RandomDNAGenerator.buildOrganism(2,
        Dice.nextInt(10) + 1,
        Dice.nextInt(10) + 1,
        Dice.nextInt(10) + 1,
        worldWidth, worldHeight
    )

//    private fun buildRandomOrganism() = RandomDNAGenerator.buildOrganism(2, 3, 3, 3, worldWidth, worldHeight)

    private fun loadRandomOrganism(): Organism {
        val dnaFormFarm = dnaFarm.readRandomDNA()
        dnaFormFarm ?: return buildRandomOrganism()
        println("read dna from farm")
        return Organism(dna = dnaFormFarm, worldWidth, worldHeight)
    }

    private fun birthNewOrganism(): Organism {
        val organismA = loadRandomOrganism()
        val organismB = loadRandomOrganism()
        val clone = Genetic.reproduce(organismA.dna, organismB.dna)
        println("----")
        println(organismA.dna)
        println(organismB.dna)
        println(clone)
        println("----")
        return Organism(dna = clone, worldWidth, worldHeight, entropy = (organismA.entropy + organismB.entropy) / 2)
    }


}
