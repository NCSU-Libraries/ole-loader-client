package edu.ncsu.lib.ole.test

/*

     Copyright (C) 2015 North Carolina State University

     This program is free software: you can redistribute it and/or modify
     it under the terms of the GNU General Public License as published by
     the Free Software Foundation, either version 3 of the License, or
     (at your option) any later version.

     This program is distributed in the hope that it will be useful,
     but WITHOUT ANY WARRANTY; without even the implied warranty of
     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
     GNU General Public License for more details.

     You should have received a copy of the GNU General Public License
     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

/**
 * Utility class for loading files froma number of locations (relative, on classpath, etc.)
 * @author Adam Constabaris
 */
class FileLoader {


    static def readResponseFromFile(String filePath) {
        def input

        input = [getClass().getResourceAsStream(filePath),
                 {
                     def file = new File(filePath);
                     if (file.exists()) {
                         input = file.newInputStream()
                     }
                 }

        ].find {
            it instanceof InputStream || (it.respondsTo("call") && it() instanceof InputStream)
        }

        if (input == null) {
            throw new IllegalArgumentException("Cant' find ${filePath} on the classpath or in the filesystem")
        }

        return input.getText("utf-8")
    }
}
