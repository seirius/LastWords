package com.lastwords.ashley.outsidetracker

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntitySystem
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.utils.ImmutableArray
import com.lastwords.ashley.stats.StatsComponent
import com.lastwords.util.STATS

class OutsideTracker: EntitySystem() {

    private lateinit var entities: ImmutableArray<Entity>

    private val trackers: MutableList<Tracker> = mutableListOf()

    override fun addedToEngine(engine: Engine?) {
        entities = engine!!.getEntitiesFor(Family.all(StatsComponent::class.java).get())
    }

    override fun update(deltaTime: Float) {
        if (entities.size() > 0 && trackers.size > 0) {
            for (tracker in trackers) {
                for (entity in entities) {
                    if (tracker.id == STATS[entity].ID) {
                        tracker.event(entity)
                    }
                }
            }
        }
    }

    fun addTrackerFor(tracker: Tracker) {
        this.trackers.add(tracker)
    }

}

class Tracker(val id: String, val event: (entity: Entity) -> Unit)

interface OutsideTrackeable {
    val outsideTracker: OutsideTracker
}