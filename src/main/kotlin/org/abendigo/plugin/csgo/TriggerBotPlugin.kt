package org.abendigo.plugin.csgo

import org.abendigo.csgo.Client.clientDLL
import org.abendigo.csgo.Client.enemies
import org.abendigo.csgo.Me
import org.abendigo.csgo.offsets.m_dwForceAttack
import org.abendigo.plugin.sleep
import org.abendigo.util.random

object TriggerBotPlugin : InGamePlugin(name = "Trigger Bot", duration = 32) {

	override val author = "Jire"
	override val description = "Fires when your crosshair is on an enemy"

	private const val MIN_SCOPE_DURATION = 64
	private const val MAX_SCOPE_DURATION = 160

	private var scopeDuration = 0

	override fun cycle() {
		val scoped = +Me.inScope
		if (!scoped) {
			scopeDuration = 0
			return
		}

		scopeDuration += duration
		if (scopeDuration < random(MIN_SCOPE_DURATION, MAX_SCOPE_DURATION)) return

		val weapon = +Me.weapon
		val weaponID = +weapon.id
		if (weaponID != 9 && weaponID != 40) return

		for ((i, e) in enemies) if (e.address == +Me.targetAddress) {
			val spotted = +e.spotted
			sleep(random(duration, if (spotted) duration * 3 else duration * 6))
			clientDLL[m_dwForceAttack] = 5.toByte()
			sleep(random(duration / 2, duration * 2))
			clientDLL[m_dwForceAttack] = 4.toByte()
		}
	}

}