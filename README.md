# `ptolemy`

Implementations of some data sets and functions in extant works of Claudius Ptolemy.

Jupyter notebooks using the code library to analyze the *Geography* with be available in a [parallel repository](https://github.com/neelsmith/ptolemy-ipynb), and available on mybinder.org.

[![Binder](https://mybinder.org/badge_logo.svg)](https://mybinder.org/v2/gh/neelsmith/ptolemy-ipynb/master)


## *Geography*

The repository currently includes a TEI edition Ptolemy's *Geography* and a code library that can parse that text into a data set of points with lon-lat locations.  The code library can also format the text as web pages in markdown suitable for publishnig with jekyll.  


Lookup a passage of the *Geography* by canonical reference (book.chapter.section):

<form onSubmit="return ptol();">
<input id="psg" type="text" maxlength="10" class="box" value="1.1.1" autofocus />
<input type="submit" class="submit" value="Lookup" />
</form>
<script>
function ptol(){
    var response = document.getElementById('psg').value;
    var newLoc = 'http://neelsmith.info/current-projects/geography/ptolemy-geography/geography-' + response + '/';
    location = newLoc;
    return false;
}
</script>


### Current version of code library:  1.5.0

See [release notes](releases.md)
