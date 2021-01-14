import java.time.*
import java.time.format.DateTimeFormatter

String dockerImage(String tag, String dockerfile = ".jenkins/Dockerfile", String buildArgs = "") {
    return docker.build(tag, "${buildArgs} -f ${dockerfile} .")
}

def get_image_version() {
    def now = LocalDateTime.now()
    return (now.format(DateTimeFormatter.ofPattern("yyyy")) + "." + \
            now.format(DateTimeFormatter.ofPattern("MM")) + "." + \
            now.format(DateTimeFormatter.ofPattern("dd")))
}

def get_image_id() {
    def last_commit_id = sh(script: "git rev-parse --short HEAD", returnStdout: true).tokenize().last()
    return (get_image_version() + "-" + last_commit_id)
}

return this
