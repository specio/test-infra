
def getDockerImage = ["SGX1-FLC": "ImagedoSGXFLC", "SGX1-FLC-KSS": "ImagedoKSS", "SGX1": "ImagedoSGX1"]

public void buildAndTest() {
	if (isUnix()) {
		sh  """
			echo "starrt"
		"""
		for (key in getDockerImage.keySet()) {
			println(key)
			println(getDockerImage[key])
		}
		sh  """
			echo "mett"
		"""
	}
}
return this
