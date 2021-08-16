package com.erdees.toyswap.model.utils

class Reputation(reputation : Long){

        val reputationDesc : String =  when {
            reputation > 1800 -> "The most trustworthy."
            reputation > 1500 -> "Trustworthy."
            reputation > 1250 -> "Great."
            reputation > 1100 -> "Good."
            reputation > 1000 -> "Average."
            reputation > 650 -> "Tarnished ."
            else -> "Infamous."
        }
    }