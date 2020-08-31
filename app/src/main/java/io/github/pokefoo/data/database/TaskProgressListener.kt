package io.github.pokefoo.data.database

interface TaskProgressListener
{
	fun setMaxProgress(max: Int)
	fun setProgress(progress: Int)
	fun onFinished()
}