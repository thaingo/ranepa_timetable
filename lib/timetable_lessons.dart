import 'package:flutter/material.dart';
import 'package:flutter/widgets.dart';
import 'package:ranepa_timetable/localizations.dart';
import 'package:ranepa_timetable/timetable_icons.dart';
import 'package:tuple/tuple.dart';

enum LessonType { Theory, Practice }

Tuple2<String, LessonType> parseLesson(String str) {
  final bracketIndex = str.indexOf('(');
  assert(bracketIndex != -1);
  final lowerTitle = str.toLowerCase();

  return Tuple2<String, LessonType>(
      str.substring(0, bracketIndex),
      (lowerTitle.contains("практ", bracketIndex) ||
              lowerTitle.contains("семин"))
          ? LessonType.Practice
          : lowerTitle.contains("лекци", bracketIndex)
              ? LessonType.Theory
              : null);
}

class Lesson {
  final String title;
  final IconData icon;

  const Lesson(this.title, this.icon);

  static Lesson fromString(BuildContext context, String str) {
    final types = LessonTypes(context);
    str = str.toLowerCase();

    if (str.contains("математик")) return types.math;
    if (str.contains("экономик")) return types.economics;
    if (str.contains("теори") && str.contains("информаци"))
      return types.informationTheory;
    if (str.contains("философи")) return types.philosophy;
    if (str.contains("культур") && str.contains("реч"))
      return types.speechCulture;
    if (str.contains("физик")) return types.physics;
    if (str.contains("хими")) return types.chemistry;
    if (str.contains("литератур")) return types.literature;
    if (str.contains("английск")) return types.english;
    if (str.contains("информатик")) return types.informatics;
    if (str.contains("географи")) return types.geography;
    if (str.contains("истори")) return types.history;
    if (str.contains("обж") ||
        (str.contains("безопасност") && str.contains("жизнедеятельност")))
      return types.lifeSafety;
    if (str.contains("биологи")) return types.biology;
    if (str.contains("общество")) return types.socialStudies;
    if (str.contains("физ") && str.contains("культур"))
      return types.physicalCulture;
    if (str.contains("этик")) return types.ethics;
    if (str.contains("менеджмент")) return types.management;
    if (str.contains("разработ") &&
        ((str.contains("програмн") && str.contains("обеспечени")) ||
            str.contains("ПО"))) return types.softwareDevelopment;
    if (str.contains("архитектур") &&
        (str.contains("эвм") || str.contains("пк")))
      return types.computerArchitecture;
    if (str.contains("операционн") && str.contains("систем"))
      return types.operatingSystems;
    if (str.contains("компьютерн") && str.contains("график"))
      return types.computerGraphic;
    if (str.contains("проектн")) return types.projectDevelopment;
    if (str.contains("баз") && str.contains("данн")) return types.databases;
    return Lesson(parseLesson(str).item1, Icons.book); // Use original title
  }
}

class LessonTypes {
  static LessonTypes _singleton;

  factory LessonTypes(BuildContext context) {
    if (_singleton == null) _singleton = LessonTypes._internal(context);

    return _singleton;
  }

  LessonTypes._internal(this.context)
      : math = Lesson(AppLocalizations.of(context).math, TimetableIcons.math),
        economics = Lesson(
            AppLocalizations.of(context).economics, TimetableIcons.economics),
        informationTheory = Lesson(
            AppLocalizations.of(context).informationTheory,
            TimetableIcons.informationTheory),
        philosophy = Lesson(
            AppLocalizations.of(context).philosophy, TimetableIcons.philosophy),
        speechCulture = Lesson(AppLocalizations.of(context).speechCulture,
            TimetableIcons.speechCulture),
        physics = Lesson(
            AppLocalizations.of(context).physics, TimetableIcons.physics),
        chemistry = Lesson(
            AppLocalizations.of(context).chemistry, TimetableIcons.chemistry),
        literature = Lesson(
            AppLocalizations.of(context).literature, TimetableIcons.literature),
        english = Lesson(
            AppLocalizations.of(context).english, TimetableIcons.english),
        informatics = Lesson(AppLocalizations.of(context).informatics,
            TimetableIcons.informatics),
        geography = Lesson(
            AppLocalizations.of(context).geography, TimetableIcons.geography),
        history = Lesson(
            AppLocalizations.of(context).history, TimetableIcons.history),
        lifeSafety = Lesson(
            AppLocalizations.of(context).lifeSafety, TimetableIcons.lifeSafety),
        biology = Lesson(
            AppLocalizations.of(context).biology, TimetableIcons.biology),
        socialStudies = Lesson(AppLocalizations.of(context).socialStudies,
            TimetableIcons.socialStudies),
        physicalCulture = Lesson(AppLocalizations.of(context).physicalCulture,
            TimetableIcons.physicalCulture),
        ethics =
            Lesson(AppLocalizations.of(context).ethics, TimetableIcons.ethics),
        management = Lesson(
            AppLocalizations.of(context).management, TimetableIcons.management),
        softwareDevelopment = Lesson(
            AppLocalizations.of(context).softwareDevelopment,
            TimetableIcons.softwareDevelopment),
        computerArchitecture = Lesson(
            AppLocalizations.of(context).computerArchitecture,
            TimetableIcons.computerArchitecture),
        operatingSystems = Lesson(AppLocalizations.of(context).operatingSystems,
            TimetableIcons.operatingSystems),
        computerGraphic = Lesson(AppLocalizations.of(context).computerGraphic,
            TimetableIcons.computerGraphic),
        projectDevelopment = Lesson(
            AppLocalizations.of(context).projectDevelopment,
            TimetableIcons.projectDevelopment),
        databases = Lesson(
            AppLocalizations.of(context).databases, TimetableIcons.databases);

  final BuildContext context;

  final Lesson math,
      economics,
      informationTheory,
      philosophy,
      speechCulture,
      physics,
      chemistry,
      literature,
      english,
      informatics,
      geography,
      history,
      lifeSafety,
      biology,
      socialStudies,
      physicalCulture,
      ethics,
      management,
      softwareDevelopment,
      computerArchitecture,
      operatingSystems,
      computerGraphic,
      projectDevelopment,
      databases;
}
