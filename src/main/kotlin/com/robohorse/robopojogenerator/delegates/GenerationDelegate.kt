package com.robohorse.robopojogenerator.delegates

import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.progress.Task.Backgroundable
import com.robohorse.robopojogenerator.errors.RoboPluginException
import com.robohorse.robopojogenerator.generator.consts.common.ClassCreator
import com.robohorse.robopojogenerator.models.GenerationModel
import com.robohorse.robopojogenerator.models.ProjectModel

internal class GenerationDelegate(
    private val classCreator: ClassCreator,
    private val environmentDelegate: EnvironmentDelegate,
    private val messageDelegate: MessageDelegate
) {

    fun runGenerationTask(
        generationModel: GenerationModel,
        projectModel: ProjectModel
    ) {
        ProgressManager.getInstance().run(object : Backgroundable(
            projectModel.project,
            TASK_TITLE, false
        ) {
                override fun run(indicator: ProgressIndicator) {
                    try {
                        classCreator.generateFiles(generationModel, projectModel)
                        messageDelegate.showSuccessMessage()
                    } catch (e: RoboPluginException) {
                        messageDelegate.onPluginExceptionHandled(e)
                    } finally {
                        indicator.stop()
                        environmentDelegate.refreshProject(projectModel)
                    }
                }
            })
    }
}

private const val TASK_TITLE = "RoboPOJO Generation"
