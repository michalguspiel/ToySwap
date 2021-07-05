package com.erdees.toyswap.model

class Reputation(reputation : Long){

        val reputationDesc : String =  when {
            reputation > 1800 -> "The most trustworthy."
            reputation > 1500 -> "Trustworthy."
            reputation > 1200 -> "Great."
            reputation > 1000 -> "Average."
            reputation > 700 -> "Tarnished ."
            else -> "Not recommended."
        }
    }