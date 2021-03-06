package com.lastwords.ashley.spells

import com.badlogic.ashley.core.*
import com.badlogic.ashley.utils.ImmutableArray
import com.badlogic.gdx.Input
import com.badlogic.gdx.math.Vector2
import com.lastwords.ashley.entities.EntityStateComponent
import com.lastwords.ashley.orders.CastOrderComponent
import com.lastwords.ashley.orders.FireSpellComponent
import com.lastwords.ashley.position.PositionComponent
import com.lastwords.ashley.stats.PropertiesComponent
import com.lastwords.ashley.stats.StatsComponent
import com.lastwords.entities.spells.Projectile

class CastSystem : EntitySystem() {

    private lateinit var fireSpellEntities: ImmutableArray<Entity>
    private lateinit var castOrderEntities: ImmutableArray<Entity>

    private val castMapper = ComponentMapper.getFor(CastComponent::class.java)
    private val entityStateMapper = ComponentMapper.getFor(EntityStateComponent::class.java)
    private val positionMapper = ComponentMapper.getFor(PositionComponent::class.java)
    private val propertiesMapper = ComponentMapper.getFor(PropertiesComponent::class.java)
    private val fireSpellMapper = ComponentMapper.getFor(FireSpellComponent::class.java)
    private val targetMapper = ComponentMapper.getFor(TargetComponent::class.java)
    private val statsMapper = ComponentMapper.getFor(StatsComponent::class.java)

    override fun addedToEngine(engine: Engine?) {
        fireSpellEntities = engine!!.getEntitiesFor(Family.all(
                CastComponent::class.java, FireSpellComponent::class.java,
                EntityStateComponent::class.java, TargetComponent::class.java,
                StatsComponent::class.java
        ).get())

        castOrderEntities = engine.getEntitiesFor(Family.all(
               CastOrderComponent::class.java, EntityStateComponent::class.java,
                CastComponent::class.java
        ).get())
    }

    override fun update(deltaTime: Float) {

        for (entity in castOrderEntities) {
            entityStateMapper.get(entity).toggleCastState()
            castMapper.get(entity).castPile.clear()
            entity.remove(CastOrderComponent::class.java)
        }

        for (entity in fireSpellEntities) {
            val fireSpellComponent = fireSpellMapper.get(entity)
            val castComponent = castMapper.get(entity)
            val entityStateComponent = entityStateMapper.get(entity)

            if (entityStateComponent.castState) {
                val spellType = Spells.tryCast(castComponent.castPile)
                if (spellType != null) {
                    castComponent.spells[fireSpellComponent.spell] = spellType
                }
            } else {
                val spell = castComponent.spells[fireSpellComponent.spell]
                val statsComponent = statsMapper.get(entity)
                if (spell != null) {
                    if (spell == SpellTypes.FIRE_BALL && statsComponent.energy >= SpellTypes.FIRE_BALL.cost) {
                        statsComponent.energy -= SpellTypes.FIRE_BALL.cost
                        engine.addEntity(Projectile(
                                positionMapper.get(entity).position.cpy(),
                                targetMapper.get(entity).target,
                                60f, propertiesMapper.get(entity).width
                        ))
                    } else if (spell == SpellTypes.FIRE_HELL && statsComponent.energy >= SpellTypes.FIRE_HELL.cost) {
                        statsComponent.energy -= SpellTypes.FIRE_HELL.cost
                        val targetComponent = targetMapper.get(entity)
                        val targetPosition = targetComponent.target
                        val positions = arrayOf(
                                targetPosition.cpy(), targetPosition.cpy().add(-5f, 0f),
                                targetPosition.cpy().add(5f, 0f), targetPosition.cpy().add(0f, 5f),
                                targetPosition.cpy().add(0f, -5f)
                        )
                        for (position in positions) {
                            engine.addEntity(Projectile(
                                    position, null, 0f, 0f, 10f
                            ))
                        }
                    }
                }
            }

            entity.remove(FireSpellComponent::class.java)
        }

    }

    companion object {
        val SPELL_KEY_MAP: HashMap<Int, String> = hashMapOf(
                Input.Keys.Q to "Q",
                Input.Keys.A to "A",
                Input.Keys.S to "S",
                Input.Keys.D to "D",
                Input.Keys.F to "F"
        )
    }

}
