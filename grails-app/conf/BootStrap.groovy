import org.openmrs.modulus.Release
import org.openmrs.modulus.Uploadable
import org.springframework.web.context.support.WebApplicationContextUtils

class BootStrap {

    def grailsApplication

    def init = { servletContext ->
        // Get spring
        def springContext = WebApplicationContextUtils.getWebApplicationContext( servletContext )
        // Custom marshalling
        springContext.getBean( "customObjectMarshallers" ).register()


        if (System.getProperty('modulus.rebuildPaths') == 'true') {
            Uploadable.list().each { Uploadable obj ->

                if (!obj.path) {
                    log.info("Not updating Uploadable id=${obj.id} due to null path")
                    return
                }

                def matcher = obj.path =~ /(.+)(org\.openmrs\.modulus.+)/
                def upDir = grailsApplication.config.modulus.uploadDestination

                def newPath = upDir + '/' + matcher[0][2]
                obj.path = newPath.replaceAll('//', '/')

                obj.save()
                log.info("Updated the path for Uploadable with id=${obj.id}")
            }

            Release.list().each { Release rel ->
                rel.generateDownloadURL()

                rel.save()
                log.info("Generated new download URL for Release id=${rel.id}")
            }
        }

    }
    def destroy = {
    }
}
