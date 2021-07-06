class UnixBuildManager {

    public void BuildAndTest() {
        if (isUnix()) {
            sh  """
                echo 'Build and test...'
                """
        }
    }

}
return UnixBuildManager();