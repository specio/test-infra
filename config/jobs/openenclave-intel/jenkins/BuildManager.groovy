
def getDockerImage = ["SGX1-FLC": "ImagedoSGXFLC", "SGX1-FLC-KSS": "ImagedoKSS", "SGX1": "ImagedoSGX1"]


public void buildAndTest() {
	if (isUnix()) {
		sh  """
			echo "starrt"
		"""
		def colors = ["SGX1-FLC": '#FF0000', "SGX1-FLC-KSS": '#00FF00', "SGX1": '#0000FF']
		for (key in colors.keySet().sort()) {
			def PLATFORM = colors[key]
			sh  """
				echo ${PLATFORM}
			"""
		}
		sh  """
			echo "mett"
		"""
	}
}
return this
