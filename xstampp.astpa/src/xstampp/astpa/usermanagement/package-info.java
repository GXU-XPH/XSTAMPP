/*******************************************************************************
 * Copyright (c) 2013, 2017 Lukas Balzer, Asim Abdulkhaleq, Stefan Wagner
 * Institute of Software Technology, Software Engineering Group
 * University of Stuttgart, Germany
 * 
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

/**
 * ADMIN:
 * <ul>
 * <li>legt project an
 * <li>erstellt user eintr�ge (name/passwort) mit standart rechten - Erkl�rung: Einlegen User und
 * User Rechten
 * <li>darf Eintr�ge(Accidents& Hazards, usw.) erstellen/bearbeiten
 * <li>darf Process Model anlegen und modifizieren
 * <li>darf Safety Constraints anlegen
 * <li>darf/muss Control Structure erstellen, kann Modifikation sperren <b>TODO</b> <i>(nur admin
 * kann modifizieren)</i>
 * <li>darf/muss ControlActions erstellen
 * <li>darf UnsafeControlActions f�r ControlActions erstellen/bearbeiten
 * <li>darf Hazard Links (verlinken) hinzuf�gen/entfernen
 * <li>darf Corresponding Safety Constraints schreiben
 * <li>darf CausalFactors anlegen
 * </ul>
 * USER:
 * <ul>
 * <li>darf Eintr�ge bearbeiten (je nach vom admin gegebenen rechten*)
 * <ol>
 * <li>Accidents
 * <li>Hazards
 * <li>Control Actions
 * <li>Safety Constraints
 * </ol>
 * <li>darf Safety Constraints anlegen/bearbeiten
 * <li>darf Control Structure einsehen und editieren <b>TODO</b> <i>(user kann control structure
 * momentan nur einsehen)</i>
 * <li>darf ControlActions bearbeiten *1
 * <li>darf UnsafeControlActions für ControlActions erstellen/bearbeiten *1<b>TODO</b>
 * <li>darf Hazard Links hinzufügen/entfernen
 * <li>darf Corresponding Safety Constraints schreiben *2<b>TODO</b>
 * <li>darf CausalFactors für UnsafeControlActions erstellen und bearbeiten *2 <b>TODO</b>
 * </ul>
 * 
 * <i>
 * * einträge können für alle/bestimmte user gesperrt werden<br>
 * *1 nur für ControlActions die für ihn vom admin freigegeben wurden <br>
 * *2 nur für zugriffsberechtigte UnsafeControlActions
 * <p>
 * </i>
 * nur ein Admin Mode, aber es kann mehrere Admins geben
 * -> es kann immer nur eine Person zu einem Zeitpunkt als Admin zugreifen<br>
 * User Profile: Standard User, Restrict User (read only)
 * 
 * @author Lukas Balzer
 *
 */
package xstampp.astpa.usermanagement;