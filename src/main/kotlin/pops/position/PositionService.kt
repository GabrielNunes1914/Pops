package pops.position

import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import pops.level.LevelRepository

@Service
class PositionService(
    private val repository: PositionRepository,
    private val positionRepository: PositionRepository,
    private val levelRepository: LevelRepository
) {

    fun findAll(): List<Position> = repository.findAll()

    fun findById(id: Long): Position = repository.findById(id)
        .orElseThrow { ResponseStatusException(HttpStatus.NOT_FOUND, "Position not found with id: $id") }

    fun create(position: Position): Position = repository.save(position)

    fun partialUpdate(id: Long, updates: Map<String, Any>): Position {
        val position = positionRepository.findById(id)
            .orElseThrow { RuntimeException("Position not found") }

        val updated = position.copy(
            name = (updates["name"] as? String) ?: position.name,
            description = (updates["description"] as? String?) ?: position.description,
            level = updates["level_id"]?.let {
                val levelId = (it as Number).toLong()
                levelRepository.findById(levelId)
                    .orElseThrow { RuntimeException("Level not found with id $levelId") }
            } ?: position.level
        )

        return positionRepository.save(updated)
    }


    fun delete(id: Long) = repository.deleteById(id)
}