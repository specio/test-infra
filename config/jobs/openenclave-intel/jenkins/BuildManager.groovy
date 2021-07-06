class BuildManager {

    public void BuildAndTest() {
        if (isUnix()) {
            sh  """
                echo 'Build and test...'
                """
        }
    }

}
return BuildManager();