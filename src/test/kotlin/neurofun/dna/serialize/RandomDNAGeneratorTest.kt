package neurofun.dna.serialize

import neurofun.dna.RandomDNAGenerator
import org.junit.jupiter.api.Test

class RandomDNAGeneratorTest {
    @Test
    fun `generate random organism`() {
        val organism = RandomDNAGenerator.buildOrganism(2, 4, 4, 4, 32, 32)
        println(organism)
        organism.step()
        println(organism)
        organism.step()
        println(organism)
    }
}