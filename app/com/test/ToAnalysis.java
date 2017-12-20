package com.test;

import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import org.ansj.domain.Result;
import org.ansj.domain.Term;
import org.ansj.recognition.arrimpl.AsianPersonRecognition;
import org.ansj.recognition.arrimpl.ForeignPersonRecognition;
import org.ansj.recognition.arrimpl.NumRecognition;
import org.ansj.recognition.arrimpl.UserDefineRecognition;
import org.ansj.splitWord.Analysis;
import org.ansj.util.AnsjReader;
import org.ansj.util.Graph;
import org.ansj.util.NameFix;
import org.ansj.util.TermUtil.InsertTermType;
import org.nlpcn.commons.lang.tire.domain.Forest;

/**
 * ��׼�ִ�
 * 
 * @author ansj
 * 
 */
public class ToAnalysis extends Analysis {

	@Override
	protected List<Term> getResult(final Graph graph) {

		Merger merger = new Merger() {
			@Override
			public List<Term> merger() {

				graph.walkPath();

				// ���ַ���
				if (isNumRecognition && graph.hasNum) {
					new NumRecognition().recognition(graph.terms);
				}

				// ����ʶ��
				if (graph.hasPerson && isNameRecognition) {
					// ��������ʶ��
					new AsianPersonRecognition().recognition(graph.terms);
					graph.walkPathByScore();
					NameFix.nameAmbiguity(graph.terms);
					// �������ʶ��
					new ForeignPersonRecognition().recognition(graph.terms);
					graph.walkPathByScore();
				}

				// �û��Զ���ʵ��ʶ��
				userDefineRecognition(graph, forests);

				return getResult();
			}

			private void userDefineRecognition(final Graph graph, Forest... forests) {
				new UserDefineRecognition(InsertTermType.SKIP, forests).recognition(graph.terms);
				graph.rmLittlePath();
				graph.walkPathByScore();
			}

			private List<Term> getResult() {
				List<Term> result = new ArrayList<Term>();
				int length = graph.terms.length - 1;
				for (int i = 0; i < length; i++) {
					if (graph.terms[i] != null) {
						result.add(graph.terms[i]);
					}
				}
				setRealName(graph, result);
				return result;
			}
		};
		return merger.merger();
	}

	public ToAnalysis() {
		super();
	}

	public ToAnalysis(Reader reader) {
		super.resetContent(new AnsjReader(reader));
	}
	
	public static Result parse(String str) {
		return new ToAnalysis().parseStr(str);
	}

	public static Result parse(String str, Forest... forests) {
		return new ToAnalysis().setForests(forests).parseStr(str);
	}

}
