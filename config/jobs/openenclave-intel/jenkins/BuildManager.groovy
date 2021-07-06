
def getDockerImage = ["SGX1-FLC": "ImagedoSGXFLC", "SGX1-FLC-KSS": "ImagedoKSS", "SGX1": "ImagedoSGX1"]

public void BuildAndTest(String imageName, String buildType, String compiler, String runArgs, String pullNumber, String oeLogLevel, String specifiedTest) {
	if (isUnix()) {
	for (key in getDockerImage.keySet()) {
		sh  """
			echo getDockerImage[key]
		"""
	}
}
