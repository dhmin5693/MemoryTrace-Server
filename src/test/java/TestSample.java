import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

// FEEDBACK 테스트 코드가 없네요. 이 기회에 공부해보시는건 어떨까요?
// https://joel-costigliola.github.io/assertj/assertj-core-features-highlight.html#exception-assertion 참고
public class TestSample {

    /*
        테스트 코드는 input에 따른 output을 검증하시면 됩니다.
        알고리즘 테스트도 같은 방식을 사용합니다.

        단위 테스트 코드가 잘 되어 있다면, run 버튼을 누를 일이 많이 줄어들거에요 :)
        내 코드의 신뢰도를 높여주기도 하구요.

        테스트는 기능 단위로 시나리오를 짜보고 그대로 작성하시면 됩니다.
        예를 들어 보자면 이런 식입니다.
        1. 교환 일기장 생성 성공
        2. 교환 일기장 생성 실패 - 로그인하지 않은 사용자
        3. 교환 일기장 생성 실패 - 교환일기에 참여하지 않은 유저가 생성함

        위 예시는 컨트롤러에서 모든 기능을 통합적으로 검증하는 시나리오가 되겠습니다.
     */
    @Test
    public void singleTestCase() {
        int expected = 3;
        int actual = sum(1, 2);

        assertThat(expected).isEqualTo(actual);
    }

    private int sum(int a, int b) {
        return a + b;
    }
}
