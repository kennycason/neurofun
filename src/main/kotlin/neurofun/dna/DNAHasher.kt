package neurofun.dna

import neurofun.dna.serialize.DNASerializer
import java.security.MessageDigest
import kotlin.text.Charsets.UTF_8

object DNAHasher {
    fun sha256Hash(dna: DNA): String {
        val dnaJsonString = DNASerializer.serialize(dna)
        val digest = MessageDigest.getInstance("SHA-256")
        val hashBytes = digest.digest(dnaJsonString.toByteArray(UTF_8))
        return hashBytes.joinToString("") { "%02x".format(it) }  // Convert bytes to hex string
    }
}

