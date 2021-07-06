
def getDockerImage = ["SGX1-FLC": "ImagedoSGXFLC", "SGX1-FLC-KSS": "ImagedoKSS", "SGX1": "ImagedoSGX1"]


public void buildAndTest() {
	if (isUnix()) {
		sh  """
			echo "starrt"
		"""
		def colors = [red: '#FF0000', green: '#00FF00', blue: '#0000FF']
		for (key in colors.keySet().sort()) {
			println(key)
			println(colors[key])
		}

		sh  """
			echo "mett"
		"""
	}
}
return this
