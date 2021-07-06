class UnixBuildManager {

    @Override
    public void BuildAndTest() {
        if (isUnix()) {
            sh  """
                echo 'Build and test...'
                """
        }
    }

}
return UnixBuildManager();