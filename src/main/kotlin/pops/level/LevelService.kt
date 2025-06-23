package pops.level

import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

@Service
class LevelService(private val repository: LevelRepository) {

    fun findAll(): List<Level> = repository.findAll()

    fun findById(id: Long): Level = repository.findById(id)
        .orElseThrow { ResponseStatusException(HttpStatus.NOT_FOUND, "Level not found with id: $id") }

    fun create(level: Level): Level = repository.save(level)

    fun partialUpdate(id: Long, updatedFields: Map<String, Any>): Level {
        val existing = findById(id)
        val updated = existing.copy(
            name = updatedFields["name"] as? String ?: existing.name,
            description = updatedFields["description"] as? String ?: existing.description
        )
        return repository.save(updated)
    }

    fun delete(id: Long) = repository.deleteById(id)
}