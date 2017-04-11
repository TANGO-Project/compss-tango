#
# This file is automatically generated by the Extrae instrumentation Makefile.
# Edit with caution
#

EXTRAE_HOME="$( cd "$( dirname "${BASH_SOURCE[0]}" )/.." && pwd )"
export EXTRAE_HOME=${EXTRAE_HOME}

if test "${EXTRAE_HOME}" != "" ; then

	# Read configuration variables if available!
	if ! test -f ${EXTRAE_HOME}/etc/extrae-vars.sh ; then
		echo "Error! Unable to locate ${EXTRAE_HOME}/etc/extrae-vars.sh"
		echo "Dying..."
		break
	else
		source ${EXTRAE_HOME}/etc/extrae-vars.sh
	fi

	if test -f "${EXTRAE_ONLINE_SOURCES}" ; then
		source ${EXTRAE_ONLINE_SOURCES}
	fi

else
	echo "You have to define EXTRAE_HOME to run this script"
fi

export PYTHONPATH=${EXTRAE_HOME}/libexec:${PYTHONPATH}

export LD_LIBRARY_PATH=${EXTRAE_MPI_LIBSDIR}:${EXTRAE_PAPI_LIBSDIR}:${EXTRAE_LIBXML2_LIBSDIR}:${EXTRAE_DYNINST_LIBSDIR}:${EXTRAE_DWARF_LIBSDIR}:${EXTRAE_ELF_LIBSDIR}:${EXTRAE_LIBERTY_LIBSDIR}:${EXTRAE_BFD_LIBSDIR}:${EXTRAE_UNWIND_LIBSDIR}:${EXTRAE_BOOST_LIBSDIR}:${LD_LIBRARY_PATH}

