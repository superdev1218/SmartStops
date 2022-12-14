package com.creativeinfoway.smartstops.app.android.navigation.v5.instruction

import com.creativeinfoway.smartstops.app.android.navigation.v5.routeprogress.RouteProgress

/**
 * Base Instruction. Subclassed to provide concrete instructions.
 *
 * @since 0.4.0
 */
abstract class Instruction {

    /**
     * Will provide an instruction based on your specifications
     *
     * @return [String] instruction that will be voiced on the client
     * @since 0.4.0
     */
    abstract fun buildInstruction(routeProgress: RouteProgress): String
}
