package io.github.pokefoo.utils

fun Any.TAG() = "${this.javaClass.name} in ${Thread.currentThread().name}"