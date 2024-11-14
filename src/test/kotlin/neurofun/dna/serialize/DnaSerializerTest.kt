package neurofun.dna.serialize

import neurofun.dna.RandomDNAGenerator
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class DnaSerializerTest {

    @Test
    fun `basic test`() {
        val dna = RandomDNAGenerator.build(2, 2, 2, 2)
        val dnaString = DNASerializer.serialize(dna)
        val dna2 = DNASerializer.deserialize(dnaString)
        println(dnaString)
        assertEquals(dna, dna2)
    }

}