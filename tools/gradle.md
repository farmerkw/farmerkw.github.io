- multi project subproject gradle 파일을 subproject 네임과 동일 하게 설정(subproject.gradle)
```gradle
// settings.gradle

include "submodule01",
        "submodule02",
        "submodule03"

def setBuildFile(project) {
    project.buildFileName = "${project.name}.gradle"
    project.children.each {
        setBuildFile(it)
    }
}

rootProject.children.each {
    setBuildFile(it)
}
```