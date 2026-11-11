package fr.uge.analysis;

import fr.uge.analysis.model.AnalysisResult;
import jakarta.inject.Singleton;

import java.nio.file.Path;

@Singleton
public interface Analyzer {
  AnalysisResult run(Path projectDirectory);
}
