<html>
<head>
<style type="text/css">
<!--
body {
	background: fixed center top no-repeat;
	background-image: url(./background.jpg); 
	background-color: black;
	color: #FFFFFF;
}

.Stil1 {color: #FFFFFF;}

a:link {
	color: #FFFFFF;
	text-decoration: none;
}

a:visited {
	color: #DCDCDC;
	text-decoration: none;
}

a:hover {
	color: #FFFFFF;
	font-weight: bolder;
	text-decoration: none;
}

a:active {
	text-decoration: none;
}
-->

</style>
<link rel="icon" type="image/png" href="./favicon.png">
<title>JGravSim</title>
<meta name="description" content="This site is about the program JGravSim, a program to calculate and visualize gravitational effects with relativistic corrections." />
<meta name="google-site-verification" content="c7AMgiZmOgNOACmriRhKmMhotamkQO34imn2ufQqSiI" />
<link rel="icon" type="image/png" href="./../favicon.png" />
</head>

<body>
<center>
<br />
<a href="http://github.com/EoD/JGravSim/commits/master"><h1>JGravSim</h1></a>
<b>A program to calculate and visualize gravitational effects with relativistic corrections!</b>
<br /><br />
<!--<b><a href="./hive">Goto the Hive</a>.</b>-->
</center>
<br />

<div align="left">
<?php
$dirpath = getcwd() . "/";
$dir = opendir($dirpath);
$files_main = array();
$files_scenario = array();
$files_docs = array();
while ($file = readdir($dir)) {
   $localpath = $dirpath.$file;
   
   if (is_file($localpath) 
		&&  (strpos($file, ".php") == false)
		&&  (strpos($file, "ndex.") == false)
		&&  (strpos($file, ".jpg") == false)
		&& !($file == "favicon.png")
		&& !($file == "robots.txt")) {
	if(strpos($file, ".wpt") != false) {
		$files_scenario[] = $file;
	}
	else if(strpos($file, ".pdf") != false) {
		$files_docs[] = $file;
	}
	else if($file == "README" ||
		$file == "COPYING" ) {
		array_unshift($files_docs, $file);
	}
	else {
#		$key = $file.filemtime($localpath).md5($file);
#		$files_main[$key] = $file;
		$files_main[] = $file;
	}
   }
}
natcasesort($files_main);
echo "<u>Main stuff:</u><br />\n";
foreach ($files_main as $file) {
	if(strpos($file, "_old") == false)
		echo "&nbsp;<small>".date ("Y/m/d", filemtime($file))."</small>&nbsp;&nbsp;<a href=\"$file\" target=\"_blank\">$file</a><br />\n";
}


$outdated = false;
echo "<br />&nbsp;<u><i>Outdated stuff:</i></u><br />\n";
foreach ($files_main as $file) {
	if(strpos($file, "_old") != false) {
		$outdated = true;
		echo "&nbsp;&nbsp;<small>".date ("Y/m/d", filemtime($file))."</small>&nbsp;&nbsp;<i><a href=\"$file\" target=\"_blank\">$file</a></i><br />\n";
	}
}
if(!$outdated) {
	echo "&nbsp;&nbsp;<i>everything is up to date</i>\n";
}

echo "<br />";
echo "<br /><u>Documents:</u><br />\n";
foreach ($files_docs as $file) {
	echo "&nbsp;<small>".date ("Y/m/d", filemtime($file))."</small>&nbsp;&nbsp;<a href=\"$file\" target=\"_blank\">$file</a><br />\n";
}

echo "<br /><u>Scenarios:</u><br />\n";
foreach ($files_scenario as $file) {
	$showname = explode (".wpt", $file);
	$showname = str_replace("_", " ", $showname);
	echo "&nbsp;<small>".date ("Y/m/d", filemtime($file))."</small>&nbsp;&nbsp;<a href=\"$file\" target=\"_blank\">$showname[0]</a><br />\n";
}
?>
</div>
</body>
</html>
