package com.testvagrant.optimus.utils

import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Paths
import java.util.stream.Collectors


class FeatureFilter {
    private List<String> tags;
    private List<File> featureFilesList;

    public FeatureFilter(List<String> tags) {
        this.tags = tags;
        featureFilesList = new ArrayList<>();
    }

    def List<File> getFilteredFeatures(List<File> featureFiles) {
        return collectFeaturesMatchingTags(featureFiles,tags);
    }

    def List<File> collectAllFeatureFilesInProject(File[] rootFile) {
        List<File> srcFiles = Arrays.stream(rootFile).filter({file -> file.getName().contains("src")}).collect(Collectors.toList());
        collectFeatureFiles(srcFiles);
        return featureFilesList;
    }

    def List<File> collectFeaturesMatchingTags(List<File> featureFiles,List<String> tags) {
        return featureFiles.stream().filter({file ->
            List<String> featureTags = formatFeatureTagList(getFeatureTags(file));
            if(featureTags.size()>0) {
                featureTags.retainAll(tags);
            }
            return featureTags.size()>0;
        }).collect(Collectors.toList());
    }

    def List<String> getFeatureTags(File file) {
        List<String> featureTags = null;
        try {
            featureTags = Files.readAllLines(Paths.get(file.getPath()), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return featureTags.stream().filter({line-> line.trim().startsWith("@")}).collect(Collectors.toList());
    }

    def void collectFeatureFiles(List<File> files) {
        for (File file : files) {
            if (file.isDirectory()) {
                collectFeatureFiles(Arrays.asList(file.listFiles())); // Calls same method again.
            } else {
                if(file.getName().endsWith(".feature")) {
                    featureFilesList.add(file);
                }
            }
        }
    }

    def List<String> formatFeatureTagList(List<String> featureTagList) {
        List<String> formattedTagsList = new ArrayList<>();
        featureTagList.forEach({tag ->
            if(tag.trim().contains(" ")) {
                List<String> collect = Arrays.stream(tag.trim().split(" ")).collect(Collectors.toList());
                formattedTagsList.addAll(collect);
            } else {
                formattedTagsList.add(tag.trim());
            }
        });
        return formattedTagsList;
    }
}
