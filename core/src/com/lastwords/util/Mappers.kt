package com.lastwords.util

import com.badlogic.ashley.core.ComponentMapper
import com.lastwords.ashley.animation.AnimationComponent
import com.lastwords.ashley.entities.EntityStateComponent
import com.lastwords.ashley.move.TrackTargetComponent
import com.lastwords.ashley.position.PositionComponent
import com.lastwords.ashley.stats.StatsComponent
import com.lastwords.ashley.texture.TextureComponent
import com.lastwords.ashley.velocity.VelocityComponent

val TRACK_TARGET: ComponentMapper<TrackTargetComponent>
        = ComponentMapper.getFor(TrackTargetComponent::class.java)
val VELOCITY: ComponentMapper<VelocityComponent>
        = ComponentMapper.getFor(VelocityComponent::class.java)
val STATS: ComponentMapper<StatsComponent>
        = ComponentMapper.getFor(StatsComponent::class.java)
val POSITION: ComponentMapper<PositionComponent>
        = ComponentMapper.getFor(PositionComponent::class.java)
val TEXTURE: ComponentMapper<TextureComponent>
        = ComponentMapper.getFor(TextureComponent::class.java)
val ANIMATION: ComponentMapper<AnimationComponent>
        = ComponentMapper.getFor(AnimationComponent::class.java)
val ENTITY_STATE: ComponentMapper<EntityStateComponent>
        = ComponentMapper.getFor(EntityStateComponent::class.java)